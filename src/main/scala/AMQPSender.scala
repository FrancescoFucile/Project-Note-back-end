import com.rabbitmq.client.ConnectionFactory

class AMQPSender(host: String) {
  val factory = new ConnectionFactory()
  factory.setHost(host)
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  def declareQueue(queueName: String) = channel.queueDeclare(queueName, false, false, false, null)

  def sendMessage(queue: String, message: String) ={
    println(s"sending amqp message $message to queue $queue")
    channel.basicPublish("", queue, null, message.getBytes("UTF-8"))}

  def closeChannel = channel.close()

  def closeConnection = connection.close()
}
