import sys

import luigi
import luigi.contrib.spark

from scrape import ScrapeDatasets


class TrainTopicModel(luigi.contrib.spark.SparkSubmitTask):
    app = 'jars/datagov-recommender-assembly-0.0.1.jar'
    entry_class = 'com.github.karlhigley.datagov.TrainTopicModel'

    def app_options(self):
        return ["--input", "data/scraper", "--output", "data/topic-model"]

    def output(self):
        return luigi.LocalTarget('lda-model')

    def requires(self):
        return ScrapeDatasets()


class FindNeighbors(luigi.contrib.spark.SparkSubmitTask):
    app = 'jars/datagov-recommender-assembly-0.0.1.jar'
    entry_class = 'com.github.karlhigley.datagov.FindNeighbors'

    def output(self):
        return luigi.LocalTarget('neighbors')

    def requires(self):
        return TrainTopicModel()


if __name__ == "__main__":
    task = FindNeighbors()
    luigi.build([task], local_scheduler=True)
