import spray.json._

case class Message(mt: String, body: Map[String,String])

val msg = Message("m1", Map("a"->"1", "b"->"2"))

import DefaultJsonProtocol._
implicit val messageConverter = jsonFormat2(Message)

val json = msg.toJson

val string = json.compactPrint

val json2 = string.parseJson

val obj = json2.convertTo[Message]

println(obj.body)

