import java.nio.file.Paths
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.FileIO
import scala.util._

trait UploadRoutes extends Directives with  TagsJsonSupport{
  this: NoteServer =>
  lazy val uploadRoute: Route = path("upload") {
    post {
      entity(as[TagsBlock]) { tagsBlock =>
        val teacher = tagsBlock.teacher
        val subject = tagsBlock.subject
        complete(s"prof $teacher on subject $subject")
      }
    }
  }
}