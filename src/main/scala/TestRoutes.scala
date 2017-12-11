import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait TestRoutes {
  lazy val testRoute: Route =
    path("HealthCheck") {
      get {
        complete("alive")
      }
    }
}
