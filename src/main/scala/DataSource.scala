import java.io.File
import java.net.URL
import java.nio.file.{Path, Paths}

import scala.sys.process._

object DataSource {
  val TITLE_FILE = "title.basics.tsv"
  val TITLE_URL = s"https://datasets.imdbws.com/$TITLE_FILE.gz"

  def downloadData(sourceFile: String): Path = {
    if (!new File(s"$sourceFile").exists) {
      (new URL(TITLE_URL) #> new File(f"$sourceFile.gz")).!
      s"gunzip $sourceFile.gz".!
    }
    Paths.get(sourceFile)
  }
}
