import scala.collection.mutable.Set
import scala.concurrent.{ExecutionContext, Future, Promise}
import spray.json._
import DefaultJsonProtocol._


import scala.util.{Failure, Success}

trait DatabaseInterface extends DBJsonSupport {

  this: NoteServer =>

  class SearchQuery(uploaderID: String) {
    val noteListJSON = Promise[String]()
    def completeQuery(noteListJSON: String) = {
      this.noteListJSON.success(noteListJSON)
    }
  }

  def handler(messageString: String): Unit = {
    println(s"processing message: $messageString")
    val message = messageString.parseJson.convertTo[DBQueryResponse]
    println(s"parsed message: $message")
    val queryID = message.query_ID
    val noteList = message.note_list.toJson.toString
    println(s"received note query response from db on amqp, query ID: $queryID, note list: $noteList")
    val searchQuery_opt = searchQueries.get(queryID)
          if (searchQuery_opt.nonEmpty) {
            val searchQuery = searchQuery_opt.get
            searchQuery.completeQuery(noteList)
          }
  }

  var searchQueries = Map[String, SearchQuery]()

  val sender = new AMQPSender("elephant.rmq.cloudamqp.com", "dmvjdchv", "dmvjdchv", "gnTdpWJY_-lNeJsTawxWELBmtuB1nGfF")
  val receiver = new AMQPReceiver("elephant.rmq.cloudamqp.com", "dmvjdchv", "dmvjdchv", "gnTdpWJY_-lNeJsTawxWELBmtuB1nGfF",  handler)

  val searchQueryRequestQueueName = "SEARCH_QUERY_REQUEST"
  sender.declareQueue(searchQueryRequestQueueName)

  val searchQueryResponseName = "SEARCH_QUERY_RESPONSE"
  receiver.declareQueue(searchQueryResponseName)

  val pageQueryQueueName = "PAGE_QUERY"
  sender.declareQueue(pageQueryQueueName)

  val pageResponseName = "PAGE_RESPONSE"
  receiver.declareQueue(pageResponseName)

  def searchNotes(searchParameters: String, searchQuery_ID: String): Future[String] = {
    val searchQuery = new SearchQuery(searchQuery_ID)
    println(s"new search query: $searchParameters")
    searchQueries += ((searchQuery_ID, searchQuery))
    sender.sendMessage(searchQueryRequestQueueName, searchParameters)
    val noteListHolder = searchQuery.noteListJSON.future
    noteListHolder
  }

  receiver.consume(searchQueryResponseName)

  /*case class PageRequest(noteID: String, page: Int, clientID: String) {
    override def toString: String = s"{noteID:$noteID,page:$page,clientID:$clientID}"
  }*/
  // TODO pageRequests as objects
  def requestPage(pageRequest: String) = {
    sender.sendMessage(pageQueryQueueName, pageRequest)
  }

}
