import luigi
from luigi.contrib.esindex import CopyToIndex

from scrape import ScrapeDatasets


class IndexDatasets(CopyToIndex):
    input_path = luigi.Parameter(default="data/scraper/datasets.json")

    host = luigi.Parameter()
    port = 9200
    index = 'datasets'
    doc_type = 'default'
    purge_existing_index = True
    marker_index_hist_size = 1
    timeout = 30

    def docs(self):
        return (line for line in open(self.input_path))

    def requires(self):
        return ScrapeDatasets(output_path=self.input_path)

if __name__ == '__main__':
    task = IndexDatasets(host="192.168.99.100")
    luigi.build([task], local_scheduler=True)
