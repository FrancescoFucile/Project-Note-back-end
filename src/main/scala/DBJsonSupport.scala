import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait DBJsonSupport extends DefaultJsonProtocol {
  case class DBMessage(messageType: String, body: Map[String,String])
  case class DBQueryResponse(queryID:String, noteList:String)
  implicit val DBMessageConverter = jsonFormat2(DBMessage)
}