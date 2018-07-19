import NoteService.testRoutes
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

trait NoteServer extends TestRoutes with SearchRoutes with PageRequestWSRoutes with WebpagesRoutes {
  implicit val system: ActorSystem = ActorSystem("ServerActorSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val mainRoute = searchRoutes ~ pageRequestWSRoutes ~ testRoutes ~ homepageRoute ~ uriTestRoute ~ uploadRoute
}
