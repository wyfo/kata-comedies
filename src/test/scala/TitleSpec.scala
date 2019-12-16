import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.testkit.scaladsl._
import org.scalatest.flatspec.AnyFlatSpec

class TitleSpec extends AnyFlatSpec {
  implicit val system: ActorSystem = ActorSystem("tests")

  "A comedy movie line" should "pass the flow" in {
    Source.single("tt0001028\tmovie\tSalome Mad\tSalome Mad\t0\t1909\t\\N\t\\N\tComedy")
          .via(Title.comedyMovies)
          .runWith(TestSink.probe[Title.OrigTitle])
          .requestNext("Salome Mad")
          .expectComplete()
  }

  "A line not being a comedy movie" should "not pass the flow" in {
    val probe = Source.single("tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t1\tDocumentary,Short")
          .via(Title.comedyMovies)
          .runWith(TestSink.probe[Title.OrigTitle])
          .expectSubscriptionAndComplete()
  }

}
