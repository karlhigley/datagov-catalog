import luigi
import luigi.contrib.spark

from scraper.scraper import PackageFetcher, ProgressCounter, write_json_file


class ScrapeDatasets(luigi.Task):
    api_url = luigi.Parameter(default="http://catalog.data.gov/api/3/")
    output_path = luigi.Parameter(default="data/scraper/datasets.json")

    def run(self):
        fetcher = PackageFetcher(self.api_url)

        write_json_file(
            self.output_path,
            fetcher.fetch_records(per_page=1000),
            ProgressCounter(fetcher.record_count).update
        )

    def output(self):
        return luigi.LocalTarget(self.output_path)

if __name__ == '__main__':
    task = ScrapeDatasets()
    luigi.build([task], local_scheduler=True)
