import luigi

from tasks import scrape, index


if __name__ == "__main__":
    luigi.run()
