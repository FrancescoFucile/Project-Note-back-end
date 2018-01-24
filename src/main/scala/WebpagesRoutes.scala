import akka.http.javadsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.{Directives, PathMatcher, Route}


trait WebpagesRoutes extends Directives {
  this: NoteServer =>

  lazy val homepageRoute =
    get {
      pathSingleSlash {
        pathEnd {
          getFromResource("testHTML.html")
        }
      }
    }


}
