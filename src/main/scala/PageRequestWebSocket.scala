import akka.NotUsed
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}
import spray.json._

trait PageRequestWebSocket {

  this: NoteServer =>

  val pageDownloadFlow: Flow[Message, Message, Any] =
  Flow[Message].collect{
    case pageRequest: TextMessage.Strict => {
      val text = pageRequest.text
      requestPage(text)
      println(s"received message on ws: $text")
      TextMessage("request sent to DB")
    }
  }
}
