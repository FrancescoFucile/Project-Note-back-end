import akka.NotUsed
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}

object DocumentUploadWebsocket {
  val documentUploadFLow: Flow[Message, Message, Any] =
  Flow[Message].collect{
    case txt: TextMessage.Strict => print(txt.text); TextMessage("Received: "+txt.text)
  }
}
