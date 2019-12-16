import akka.Done
import akka.stream.alpakka.amqp.scaladsl.AmqpSink
import akka.stream.alpakka.amqp.{AmqpDetailsConnectionProvider, AmqpUriConnectionProvider, AmqpWriteSettings, QueueDeclaration}
import akka.stream.scaladsl._
import akka.util.ByteString
import com.rabbitmq.client.ConnectionFactory

import scala.concurrent.Future

// First version using java rabbitmq client, kept for glory!
class LapinMessager(uri: String, queueName: String, log: Boolean) {
  private val factory = new ConnectionFactory()
  factory.setUri(uri)
  private val connection = factory.newConnection()
  private val channel = connection.createChannel()
  channel.queueDeclare(queueName, false, false, false, null)

  def publish(msg: String): Unit = {
    if (log) println(msg) // kind of logging
    channel.basicPublish("", queueName, null, msg.getBytes)
  }

  def close(): Unit = {
    channel.close()
    connection.close()
  }
}

object LapinMessager {
  val DEFAULT_URI = "amqp://guest:guest@localhost:5672/%2F"

  def sink(uri: String, queueName: String, log: Boolean): Sink[String, Future[Done]] = {
    val connectionProvider = AmqpUriConnectionProvider(uri)
    val queueDeclaration = QueueDeclaration(queueName)
    val sink = AmqpSink.simple( // I have wanted to use a Flow, but AmpqFlow(PR #1921) has not seemed to be be released yet
      AmqpWriteSettings(connectionProvider).withRoutingKey(queueName).withDeclaration(queueDeclaration)
      ).contramap[String](ByteString(_))
    if (log) Flow[String].map(s => {
      println(s); s // kind of logging
    }).toMat(sink)(Keep.right)
    else sink
  }

}
