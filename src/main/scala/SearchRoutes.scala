import akka.http.scaladsl.server.{Directives, PathMatcher, Route}
import scala.util._


trait SearchRoutes extends Directives with DatabaseInterface with PageRequestWebSocket {

  this: NoteServer =>

  lazy val searchRoutes =
    path("search") {
      get {
          parameters('subject.as[String], 'teacher.as[String]) {
            (subject, teacher) =>
              val AMQPMessage = s"{subject:$subject teacher:$teacher}"
              println(AMQPMessage)
                onComplete(searchNotes(AMQPMessage)) {
                  case Success(noteList) => complete(noteList)
                  case Failure(ex) => complete(s"error: $ex")
                }
          }
      }
    }
}
