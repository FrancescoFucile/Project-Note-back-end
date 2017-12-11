import scala.collection.mutable.Set
import scala.concurrent.{ExecutionContext, Future, Promise}
import spray.json._

import scala.util.{Failure, Success}

trait DatabaseInterface extends DatabaseInterfaceConfig with DBJsonSupport {

  this: NoteServer =>

  class Uploader(uploaderID: String) {
    val noteID = Promise[String]()

    def completeUploader(receiverNoteID: String) = {
      noteID.success(receiverNoteID)
    }
  }

  def handler(messageString: String): Unit = {
    println(s"processing message: $messageString")
    val message = messageString.parseJson.convertTo[DBMessage]
    println(s"parsed message: $message")
    val messageType = message.messageType
    val messageBody = message.body
    messageType match {
      case "uploadResponse" =>
        println("RECEIVER UPLOAD RESPONSE")
        val uploaderIDopt = messageBody.get("uploaderID")
        val noteIDOpt = messageBody.get("noteID")
        if (uploaderIDopt.nonEmpty && noteIDOpt.nonEmpty) {
          val uploaderID = uploaderIDopt.get
          val noteID = noteIDOpt.get
          println(s"uploaderID: $uploaderID\nnoteID: $noteID")
          val uploaderOpt = uploaders.get(uploaderID)
          if (uploaderOpt.nonEmpty) {
            val uploader = uploaderOpt.get
            uploader.completeUploader(noteID)
          }
        }
      case _ => println("MESSAGE NOT RECOGNIZED")
    }
  }

  var uploaders = Map[String, Uploader]()

  val sender = new AMQPSender(DBUrl)
  val receiver = new AMQPReceiver("localhost", handler)

  val uploadRequestQueueName = "NOTE_UPLOAD_REQUEST"
  sender.declareQueue(uploadRequestQueueName)

  val uploadResponseQueueName = "NOTE_UPLOAD_RESPONSE"
  receiver.declareQueue(uploadResponseQueueName)

  val streamingQueueName = "STREAMING_QUEUE"
  sender.declareQueue(streamingQueueName)

  def uploadNote(uploaderID: String): Future[String] = {
    println(s"uploading note $uploaderID")
    val uploader = new Uploader(uploaderID)
    uploaders += ((uploaderID, uploader))
    sender.sendMessage(uploadRequestQueueName, uploaderID)
    val noteIDHolder = uploader.noteID.future
    noteIDHolder
  }

  receiver.consume(uploadResponseQueueName)

  case class Page(noteID: String, page: Int) {
    override def toString: String = s"{noteID:$noteID,page:$page}"
  }

  def streamPage(noteID: String, page: Int) = {
    sender.sendMessage(streamingQueueName, Page(noteID, page).toString)
  }


}
