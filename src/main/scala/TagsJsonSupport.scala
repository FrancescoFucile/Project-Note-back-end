import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait TagsJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val tagsBlockFormat = jsonFormat2(TagsBlock)
}
