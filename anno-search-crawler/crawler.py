from bs4 import BeautifulSoup
import requests
from urllib.parse import urlparse
import os
import utils


scanned_urls = []
outer_links = []
annotated = utils.load_scanned_urls()


def should_visit(url):
    path = urlparse(url).path
    extension = os.path.splitext(path)[1]

    if extension != "":
        return False

    if url in scanned_urls or url in outer_links:
        return False

    if utils.is_erroneous_address(url):
        return False

    return True


def scan_page(url, base_url):
    if should_visit(url):
        request = requests.get(url)
        html = request.text
        soup = BeautifulSoup(html, "html.parser")

        print(url)
        if soup.title:
            if url not in scanned_urls:
                scanned_urls.append(url)
                if url not in annotated:
                    utils.process_doc(html)
                    utils.add_to_scanned_urls(url)

            for link in soup.find_all("a"):
                new_link = utils.prepare_link(url, link.get("href"))
                soup = html = request = None  # clean(request, html, soup)
                if not utils.is_outer_url(new_link, base_url):
                    scan_page(new_link, base_url)
                elif utils.is_outer_url(new_link, base_url):
                    outer_links.append(new_link)
                    scan_page(new_link, base_url)
    else:
        return

    return scanned_urls


def crawl():
    with open("meta/websites.txt", "r") as file:
        websites = file.read().split("\n")

    for item in websites:
        base_url = item
        scan_page(item, base_url)
        del scanned_urls[:]


crawl()
