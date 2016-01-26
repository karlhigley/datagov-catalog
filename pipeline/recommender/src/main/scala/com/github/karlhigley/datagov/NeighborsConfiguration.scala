package com.github.karlhigley.datagov

class NeighborsConfiguration(args: Array[String]) extends Serializable {
  var inputPath = "./lda-model"
  var outputPath = "./neighbors"
  var dimensions = 100
  var length = 256
  var tables = 1
  var neighbors = 10

  parse(args.toList)

  private def parse(args: List[String]): Unit = args match {
    case ("--input" | "-i") :: path :: tail =>
      inputPath = path
      parse(tail)

    case ("--output" | "-o") :: path :: tail =>
      outputPath = path
      parse(tail)

    case ("--dimensions" | "-d") :: value :: tail =>
      dimensions = value.toInt
      parse(tail)

    case ("--length" | "-l") :: value :: tail =>
      length = value.toInt
      parse(tail)

    case ("--tables" | "-t") :: value :: tail =>
      tables = value.toInt
      parse(tail)

    case ("--neighbors" | "-n") :: value :: tail =>
      neighbors = value.toInt
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
      |   -i PATH, --input PATH          Relative path of input files (default: "./lda-model")
      |   -o PATH, --output PATH         Relative path of output files (default: "./neighbors")
      |   -d VALUE, --dimensions VALUE   Number of topic vector dimensions (default: 100)
      |   -l VALUE, --length VALUE       Length of locality-sensitive hash signatures (default: 256)
      |   -t VALUE, --tables VALUE       Number of hash tables (default: 1)
      |   -n VALUE, --neighbors VALUE    Number of neighbors to return (default: 10)
     """.stripMargin
    System.err.println(usage)
    System.exit(exitCode)
  }

}
