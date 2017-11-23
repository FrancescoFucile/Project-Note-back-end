import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait TestRoutes {
  lazy val testRoute: Route =
    pathSingleSlash {
      get {
        complete("questo è un test.")
      }
    }
}
