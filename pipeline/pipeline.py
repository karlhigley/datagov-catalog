import luigi

from tasks import scrape, index, recommend


if __name__ == "__main__":
    luigi.run()
