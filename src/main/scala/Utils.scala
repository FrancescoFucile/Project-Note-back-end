import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader

object Utils {
  def makeHttpResponse(content: String): HttpResponse = {
    val headers = List(HttpHeader)
    val head_1 = RawHeader("Access-Control-Allow-Origin", "*")
    val head_2 = RawHeader("Content-Type","text/plain; charset=UTF-8")
    val head_3 = RawHeader("Content-Length", content.length.toString)
    val resp = new HttpResponse(200, List(head_1, head_2, head_3), content, HttpProtocols.`HTTP/1.1`)
    resp
  }
}
