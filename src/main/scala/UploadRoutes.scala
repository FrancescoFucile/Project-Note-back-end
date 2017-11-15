import java.nio.file.Paths

import akka.http.scaladsl.server.{Directives, PathMatcher, Route}
import akka.stream.scaladsl.FileIO

import scala.util._

trait UploadRoutes extends Directives with TagsJsonSupport {
  this: NoteServer =>

  import DocumentUploadWebsocket._

  lazy val uploadRoute: Route =
    post {
      pathPrefix("upload") {
        pathEnd {
          entity(as[TagsBlock]) { tagsBlock =>
            val teacher = tagsBlock.teacher
            val subject = tagsBlock.subject
            complete(s"prof $teacher on subject $subject")
          }
        }/* ~
        path(Remaining) { string: String => print(string)
          handleWebSocketMessages(documentUploadFLow)
        }*/
      }
    } ~
      get {
        path("ws") {
          handleWebSocketMessages(documentUploadFLow)
        }
      }
}