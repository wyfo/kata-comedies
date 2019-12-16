import akka.NotUsed
import akka.stream.scaladsl.Flow

case class Title(title: Title.OrigTitle, `type`: Title.Type, genres: Array[Title.Genre])

object Title {
  type OrigTitle = String
  type Type = String
  type Genre = String

  object Type {
    val MOVIE: Type = "movie"
  }

  object Genre {
    val COMEDY: Genre = "Comedy"
  }

  val LINE_MAX_LENGTH: Int = 10 * 1024 // arbitrary number found there: https://github.com/akka/alpakka/blob/master/csv/src/main/scala/akka/stream/alpakka/csv/scaladsl/CsvParsing.scala

  def fromLine(line: String): Title = {
    val split = line.split("\t") // TSV file
    Title(split(3), split(1), split(8).split(","))
  }

  val comedyMovies: Flow[String, OrigTitle, NotUsed] = Flow[String]
    .map(Title.fromLine)
    .filter(_.genres.contains(Title.Genre.COMEDY))
    .filter(_.`type` == Title.Type.MOVIE)
    .map(_.title)
}

