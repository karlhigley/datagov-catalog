import sys
import json
import requests

from math import ceil
from math import floor


class ProgressCounter:

    def __init__(self, total_items):
        self.total = total_items

    def update(self, completed):
        message = self._generate_progress_message(completed, self.total)
        self._overwrite_line(message)

    @staticmethod
    def _generate_progress_message(completed, total):
        percentage = int(floor(float(completed) / total * 100))
        return "Completed: {0}/{1} ({2}%)".format(
            completed, total, percentage
        )

    @staticmethod
    def _overwrite_line(line):
        sys.stdout.write('%s\r' % line)
        sys.stdout.flush()


class CKANClient:

    def __init__(self, base_url):
        self.base_url = base_url

    def package_search(self, **kwargs):
        return self._action(self.base_url, "package_search", **kwargs)['result']

    @staticmethod
    def _action(base_url, action, **kwargs):
        action_url = base_url + "/action/" + action
        response = requests.get(action_url, params=kwargs)
        return response.json()


class PackageFetcher:

    def __init__(self, base_url):
        self.client = CKANClient(base_url)
        self.record_count = self.client.package_search(
            rows=1, start=1)['count']

    def fetch_records(self, per_page):
        offsets = self._compute_offsets(self.record_count, per_page)
        for page in self._fetch_pages(self.client, offsets, per_page):
            for record in page:
                yield record

    @staticmethod
    def _fetch_pages(client, offsets, per_page):
        for offset in offsets:
            yield client.package_search(rows=per_page, start=offset)['results']

    @staticmethod
    def _compute_offsets(record_count, per_page):
        page_count = int(ceil(record_count / per_page))
        return [page_number * per_page + 1 for page_number in range(page_count)]


def compact_json(obj):
    return json.dumps(obj, separators=(',', ':'))


def write_json_file(filename, iterable, callback=lambda _: None):
    with open(filename, 'w') as f:
        callback(0)
        for index, item in enumerate(iterable, start=1):
            f.write(compact_json(item) + "\n")
            callback(index)


if __name__ == "__main__":
    if len(sys.argv) < 3:
        print "scraper.py <api_url> <output_file>"
        sys.exit(1)
    api_url, output_file = sys.argv[1], sys.argv[2]

    fetcher = PackageFetcher(api_url)

    write_json_file(
        output_file,
        fetcher.fetch_records(per_page=1000),
        ProgressCounter(fetcher.record_count).update
    )
