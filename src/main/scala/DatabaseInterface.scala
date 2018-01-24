import scala.collection.mutable.Set
import scala.concurrent.{ExecutionContext, Future, Promise}
import spray.json._

import scala.util.{Failure, Success}

trait DatabaseInterface extends DatabaseInterfaceConfig with DBJsonSupport {

  this: NoteServer =>

  class SearchQuery(uploaderID: String) {
    val noteListJSON = Promise[String]()

    def completeQuery(noteListJSON: String) = {
      this.noteListJSON.success(noteListJSON)
    }
  }

  def handler(messageString: String): Unit = {
    println(s"processing message: $messageString")
    val message = messageString.parseJson.convertTo[DBMessage]
    println(s"parsed message: $message")
    val messageType = message.messageType
    val messageBody = message.body
    messageType match {
      case "note_query" => {
        println("received note query")
        val queryID_opt = messageBody.get("query_ID")
        val noteList_opt = messageBody.get("note_list")
        if (queryID_opt.nonEmpty && noteList_opt.nonEmpty) {
          val queryID = queryID_opt.get
          val noteList = noteList_opt.get
          println(s"query ID: $queryID\nnote list: $noteList")
          val searchQuery_opt = searchQueries.get(queryID)
          if (searchQuery_opt.nonEmpty) {
            val searchQuery = searchQuery_opt.get
            searchQuery.completeQuery(noteList)
          }
        }
      }
      case _ => println("error: message not recognized")
    }
  }

  var searchQueries = Map[String, SearchQuery]()

  val sender = new AMQPSender(DBUrl)
  val receiver = new AMQPReceiver("localhost", handler)

  val searchQueryRequestQueueName = "SEARCH_QUERY_REQUEST"
  sender.declareQueue(searchQueryRequestQueueName)

  val searchQueryResponseName = "SEARCH_QUERY_RESPONSE"
  receiver.declareQueue(searchQueryResponseName)

  val pageQueryQueueName = "PAGE_QUERY"
  sender.declareQueue(pageQueryQueueName)

  def searchNotes(searchQuery_ID: String): Future[String] = {
    println(s"new search query: $searchQuery_ID")
    val searchQuery = new SearchQuery(searchQuery_ID)
    searchQueries += ((searchQuery_ID, searchQuery))
    sender.sendMessage(searchQueryRequestQueueName, searchQuery_ID)
    val noteListHolder = searchQuery.noteListJSON.future
    noteListHolder
  }

  receiver.consume(searchQueryResponseName)

  /*case class PageRequest(noteID: String, page: Int, clientID: String) {
    override def toString: String = s"{noteID:$noteID,page:$page, clientID: $clientID}"
  }*/

  def requestPage(pageRequest: String) = {
    sender.sendMessage(pageQueryQueueName, pageRequest)
  }

}
