import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object NoteService extends App with NoteServer {

  println("server started")

  val serverBindingFuture: Future[ServerBinding] = Http().bindAndHandle(mainRoute, "0.0.0.0", sys.env("PORT").toInt)
}
