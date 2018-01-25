import akka.http.javadsl.model.ContentTypes
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, PathMatcher, Route}
import Utils._


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

  lazy val uriTestRoute =
    get {
      path("uritest") {
        pathEndOrSingleSlash {
          println("requested")
          val head = headers.`Access-Control-Allow-Origin`.*
          val content = """{"list":[{"name":"my name","address":"myAddress"},{"name":"my name 2","address":"myAddress2"}]}"""
          val resp = new HttpResponse(200, List(head), content, HttpProtocols.`HTTP/1.1`)
          complete(resp)
        }
      }
    }


}
