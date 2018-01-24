import akka.http.scaladsl.server.{Directives, PathMatcher, Route}
import scala.util._


trait PageRequestWSRoutes extends Directives with TagsJsonSupport with DatabaseInterface with PageRequestWebSocket {

  this: NoteServer =>

  lazy val pageRequestWSRoutes =
    get {
      path("view_note") {
        pathEnd {
          handleWebSocketMessages(pageDownloadFlow)
        }
      }
    }
}
