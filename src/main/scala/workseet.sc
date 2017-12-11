import scala.concurrent.Promise

lazy val p = Promise[String]()
p.future.onComplete(Try[String] => println("completed"))
p.success("hello")
