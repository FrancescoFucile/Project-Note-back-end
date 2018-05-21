import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.{Directives, PathMatcher, Route}
import scala.util._


trait SearchRoutes extends Directives with DatabaseInterface with PageRequestWebSocket {

  this: NoteServer =>

  lazy val searchRoutes =
    path("search") {
      get {
        respondWithHeaders(RawHeader("Access-Control-Allow-Origin","*")) {
          parameters('subject,  'language, 'teacher.?, 'university.?, 'year.?) {
            (subject, teacher, university, year, language) =>
              println(s"received parameters: $subject, $language, $teacher, $university, $year")
              val searchQuery_ID = (Math.random()*100000).toInt.toString
              val AMQPMessage = makeSearchJSON(searchQuery_ID, subject, teacher, university, year, language)
              println("sending amqp message to db: " + AMQPMessage)
              onComplete(searchNotes(AMQPMessage, searchQuery_ID)) {
                case Success(noteList) => complete(noteList)
                case Failure(ex) => complete(s"error: $ex")
              }
          }
        }
      }
    }

  def makeSearchJSON(query_ID:String, subject:String,  language:String, teacher_opt:Option[String], university_opt:Option[String], year_opt:Option[String]): String = {
    var res = s"""{"query_ID":"$query_ID","subject":"$subject","language":"$language""""
    if(teacher_opt.nonEmpty) {
      val teacher = teacher_opt.get
      res = res + s""","teacher":"$teacher""""
    }
    if(university_opt.nonEmpty) {
      val university = university_opt.get
      res = res + s""","university":"$university""""
    }
    if(year_opt.nonEmpty) {
      val year = year_opt.get
      res = res + s""","year":"$year""""
    }
    res + "}"
  }

}