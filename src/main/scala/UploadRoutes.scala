import java.nio.file.Paths

import akka.http.scaladsl.server.{Directives, PathMatcher, Route}
import akka.stream.scaladsl.FileIO

import scala.util._

trait UploadRoutes extends Directives with TagsJsonSupport with DatabaseInterface {
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
        } /* ~
        path(Remaining) { string: String => print(string)
          handleWebSocketMessages(documentUploadFLow)
        }*/
      } ~
        pathPrefix("tryUpload") {
          pathEnd {
            entity(as[String]) { string =>
              println("received upload request")
              onComplete(uploadNote(string)){
                case Success(noteID) => complete (s"received server response: $noteID")
                case Failure(ex) => complete (s"failed to contact server: $ex")
              }
            }
          }
        }
    } ~
      get {
        path("ws") {
          handleWebSocketMessages(documentUploadFLow)
        }
      }
}