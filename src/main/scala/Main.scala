import java.net.ConnectException

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Main extends App {
  val config = Config.parse_args(args)
  val source = FileIO.fromPath(DataSource.downloadData(config.sourceFile))

  implicit val system: ActorSystem = ActorSystem("ComedyTitles")
  private implicit val ec: ExecutionContext = system.dispatcher

  source.via(Framing.delimiter(ByteString("\n"), Title.LINE_MAX_LENGTH).map(_.utf8String))
        .via(Title.comedyMovies)
        .runWith(LapinMessager.sink(config.rabbitMqUri, config.queueName, config.verbose))
        .onComplete(done => {
          system.terminate();
          done match {
            case Failure(e: Throwable) => println(e.getMessage)
            case Success(_) =>
          }
        })
}
