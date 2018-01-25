import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model._

object Utils {
  def makeHttpResponse(content: String): HttpResponse = {
    val head = headers.`Access-Control-Allow-Origin`.*
    val resp = new HttpResponse(200, List(head), content, HttpProtocols.`HTTP/1.1`)
    resp
  }
}
