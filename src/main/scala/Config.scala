case class Config(sourceFile: String,
                  rabbitMqUri: String,
                  queueName: String,
                  verbose: Boolean)

object Config {
  val DEFAULT_CONFIG: Config = Config(sourceFile = s"${DataSource.TITLE_FILE}",
                                      rabbitMqUri = LapinMessager.DEFAULT_URI,
                                      queueName = "comedy_movies",
                                      verbose = false)
  val FILE_PARAMETER = "file"
  val URI_PARAMETER = "rabbimq-uri"
  val QUEUE_PARAMETER = "queue"

  private def _parse(args: List[String], config: Config): Config = {
    args match {
      case Nil => config
      case s"--${FILE_PARAMETER}" :: file :: tail => _parse(tail, config.copy(sourceFile = file))
      case s"--${URI_PARAMETER}" :: uri :: tail => _parse(tail, config.copy(rabbitMqUri = uri))
      case s"--${QUEUE_PARAMETER}" :: queue :: tail => _parse(tail, config.copy(queueName = queue))
      case "--verbose" :: tail => _parse(tail, config.copy(verbose = true))
      case arg => println(s"Invalid Argument $arg"); sys.exit(1)
    }

  }

  def parse_args(args: Array[String], config: Config = DEFAULT_CONFIG): Config = {
    if (args sameElements Array("--help")) {
      println(s"Usage: [--$FILE_PARAMETER <title file>] [--$URI_PARAMETER <rabbitmq uri>] [--$QUEUE_PARAMETER <queue name>] [--verbose]")
      println(s"default values: ${FILE_PARAMETER}=${DEFAULT_CONFIG.sourceFile} | ${URI_PARAMETER}=${DEFAULT_CONFIG.rabbitMqUri} | ${QUEUE_PARAMETER}=${DEFAULT_CONFIG.queueName}")
      println("(title file will be downloaded if missing)")
      println("(verbose means printing the titles sent to RabbitMQ)")
      sys.exit(0)
    }
    _parse(args.toList, DEFAULT_CONFIG)
  }
}