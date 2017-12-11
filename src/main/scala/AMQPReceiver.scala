import java.util.regex.Pattern

import com.rabbitmq.client._

class AMQPReceiver(host: String, handler: String => Unit ) {

  val factory = new ConnectionFactory()
  factory.setHost(host)
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  def declareQueue(queueName: String) = channel.queueDeclare(queueName, false, false, false, null)

  val consumer = new DefaultConsumer(channel) {

    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]) {
      var message = new String(body, "UTF-8")
      handler(message)
    }
  }

  def consume(queueName: String) = channel.basicConsume(queueName, true, consumer)
}
