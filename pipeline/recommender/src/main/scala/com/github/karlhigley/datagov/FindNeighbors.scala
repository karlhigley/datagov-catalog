package com.github.karlhigley.datagov

import org.apache.spark.{ SparkContext, SparkConf, Logging }
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.clustering.DistributedLDAModel
import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.storage.StorageLevel

import com.github.karlhigley.spark.neighbors.ANN

object FindNeighbors extends Logging {
  val serializerClasses: Array[Class[_]] = Array()

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("Data.gov Recommender")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    sparkConf.registerKryoClasses(serializerClasses)
    val sc = new SparkContext(sparkConf)

    val config = new NeighborsConfiguration(args)

    val distModel = DistributedLDAModel.load(sc, config.inputPath)
    val documentTopics = distModel.topTopicsPerDocument(config.dimensions / 10)

    val topicVectors =
      documentTopics
        .map {
          case (id, indices, values) =>
            (id.toInt, new SparseVector(config.dimensions, indices, values))
        }
        .repartition(256)
        .persist(StorageLevel.MEMORY_AND_DISK_SER)

    topicVectors.count()

    val ann =
      new ANN(config.dimensions, "cosine")
        .setTables(config.tables)
        .setSignatureLength(config.length)

    val model = ann.train(topicVectors)
    val neighbors = model.neighbors(config.neighbors)

    neighbors.saveAsObjectFile(config.outputPath)

    sc.stop()
  }
}
