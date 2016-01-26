package com.github.karlhigley.datagov

class TopicModelConfiguration(args: Array[String]) {
  var inputPath = "./datasets-json"
  var outputPath = "./lda-model"
  var topics = 100
  var iterations = 20

  parse(args.toList)

  private def parse(args: List[String]): Unit = args match {
    case ("--input" | "-i") :: path :: tail =>
      inputPath = path
      parse(tail)

    case ("--output" | "-o") :: path :: tail =>
      outputPath = path
      parse(tail)

    case ("--topics" | "-t") :: value :: tail =>
      topics = value.toInt
      parse(tail)

    case ("--iterations" | "-r") :: value :: tail =>
      iterations = value.toInt
      parse(tail)

    case ("--help" | "-h") :: tail =>
      printUsageAndExit(0)

    case _ =>
  }

  /**
   * Print usage and exit JVM with the given exit code.
   */
  private def printUsageAndExit(exitCode: Int) {
    val usage =
      s"""
      |Usage: spark-submit --class io.github.karlhigley.Summarizer <jar-path> [summarizer options]
      |
      |Options:
      |   -i PATH, --input PATH          Relative path of input files (default: "./datasets-json")
      |   -o PATH, --output PATH         Relative path of output files (default: "./lda-model")
      |   -t VALUE, --topics VALUE       Number of topics to train (default: 100)
      |   -r VALUE, --iterations VALUE   Maximum number of iterations (default: 20)
     """.stripMargin
    System.err.println(usage)
    System.exit(exitCode)
  }

}
