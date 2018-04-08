import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait DBJsonSupport extends DefaultJsonProtocol {
  case class DBQueryResponse(query_ID:String, note_list:List[Map[String,String]])
  implicit val DBQueryResponseJSONConverter = jsonFormat2(DBQueryResponse)
}