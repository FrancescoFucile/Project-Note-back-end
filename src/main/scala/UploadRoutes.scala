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
        fileUpload("note") {
          case (fileInfo, fileStream) =>
            val sink = FileIO.toPath(Paths.get("/tmp") resolve fileInfo.fileName)
            val writeResult = fileStream.runWith(sink)
            onSuccess(writeResult) { result =>
              result.status match {
                case Success(_) => complete(s"Successfully written ${result.count} bytes")
                case Failure(e) => throw e
              }
            }
        }
      }
    }
  }
}