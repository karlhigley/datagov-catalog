package com.github.karlhigley.datagov

import chalk.text.analyze.PorterStemmer
import chalk.text.tokenize.SimpleEnglishTokenizer

import org.apache.spark.{ SparkContext, SparkConf, Logging }
import org.apache.spark.mllib.clustering.{ LDA, DistributedLDAModel, LDAModel }
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

object TrainTopicModel extends Logging {
  val serializerClasses: Array[Class[_]] = Array()

  private def stem(token: String): String = {
    PorterStemmer(token)
  }

  private def tokenize(texts: RDD[(Long, String)]): RDD[(Long, Array[String])] = {
    val tokenizer = SimpleEnglishTokenizer()
    val nonWord = "[^a-z]*".r

    texts.mapValues(s =>
      tokenizer(s.toLowerCase)
        .toArray
        .map(nonWord.replaceAllIn(_, ""))
        .filter(_.length > 3)
        .map(stem))
  }

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("Data.gov Recommender")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    sparkConf.registerKryoClasses(serializerClasses)
    val sc = new SparkContext(sparkConf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    val config = new TopicModelConfiguration(args)

    val corpus = sqlContext.read.json(config.inputPath)

    val descriptions = corpus.select("notes").rdd.map {
      case Row(notes: String) => notes
      case _ => ""
    }.zipWithIndex.map(_.swap).cache()

    val tokens = tokenize(descriptions)

    val hashingTF = new HashingTF()
    val termFrequencies = tokens.mapValues(t => {
      hashingTF.transform(t)
    })

    val ldaModel = new LDA()
      .setK(config.topics)
      .setMaxIterations(config.iterations)
      .run(termFrequencies)

    // Since LDAModel doesn't have a topTopicsPerDocument method,
    // the model has to be converted to a DistributedLDAModel
    // and saving/reloading is currently the only way to do that
    ldaModel.save(sc, config.outputPath)

    sc.stop()
  }
}
