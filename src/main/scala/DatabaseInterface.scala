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

  def handler(message: String): Unit = {

    val messageType = message.parseJson.convertTo[DBMessage].messageType
    val messageBody = message.parseJson.convertTo[DBMessage].body
    messageType match {
      case "uploadResponse" =>
        println("RECEIVER UPLOAD RESPONSE")
        val uploaderIDopt = messageBody.get("uploaderID")
        val noteIDOpt = messageBody.get("noteID")
        if(uploaderIDopt.nonEmpty && noteIDOpt.nonEmpty){
          val uploaderID = uploaderIDopt.get
          val noteID = noteIDOpt.get
          val uploaderOpt = uploaders.get(uploaderID)
          if(uploaderOpt.nonEmpty){
            val uploader = uploaderOpt.get
            uploader.completeUploader(noteID)
          }
        }
      case _ => println("MESSAGE NOT RECOGNIZED")
    }
  }

  var uploaders = Map[String,Uploader]()

  val sender = new AMQPSender(DBUrl)
  val receiver = new AMQPReceiver(DBUrl, handler)

  val uploadQueueName = "NOTE_UPLOAD"
  sender.declareQueue(uploadQueueName)

  val streamingQueueName = "STREAMING_QUEUE"
  sender.declareQueue(streamingQueueName)

  def uploadNote(uploaderID: String): String = {
    println(s"uploading note $uploaderID")
    val uploader = new Uploader(uploaderID)
    uploaders += ((uploaderID, uploader))
    sender.sendMessage(uploadQueueName, uploaderID)
    val noteIDHolder = uploader.noteID.future
    var noteID: String = ""
    noteIDHolder.onComplete{
      case Success(string) =>
        println("SENDING RESPONSE TO CLIENT")
        noteID = string
      case Failure(_) => "ERROR"
    }
    noteID
  }

  case class Page(noteID: String, page: Int) {
    override def toString: String = s"{noteID:$noteID,page:$page}"
  }

  def streamPage(noteID: String, page: Int) = {
    sender.sendMessage(streamingQueueName, Page(noteID, page).toString)
  }


}
