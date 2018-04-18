import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.{Directives, PathMatcher, Route}
import scala.util._


trait SearchRoutes extends Directives with DatabaseInterface with PageRequestWebSocket {

  this: NoteServer =>

  lazy val searchRoutes =
    path("search") {
      get {
        respondWithHeaders(RawHeader("Access-Control-Allow-Origin","*")) {
          parameters('subject.as[String], 'teacher.as[String]) {
            (subject, teacher) =>
              //val searchQuery_ID = (Math.random()*100000).toInt.toString
              val searchQuery_ID = 123456789.toString
              val AMQPMessage = s"{subject:$subject,teacher:$teacher,queryID:$searchQuery_ID}"
              println("sending amqp message to db: " + AMQPMessage)
              onComplete(searchNotes(AMQPMessage, searchQuery_ID)) {
                case Success(noteList) => complete(noteList)
                case Failure(ex) => complete(s"error: $ex")
              }
          }
        }
      }
    }
}
