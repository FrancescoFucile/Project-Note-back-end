import NoteService.testRoute
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

trait NoteServer extends TestRoutes with UploadRoutes {
  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  val mainRoute = testRoute ~ uploadRoute
}
