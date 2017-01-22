from urllib.parse import urljoin
import time
import json
import requests


forbidden_chars = ["#", "share", "[", "]"]

endpoint = "https://text.s4.ontotext.com/v1/news"
api_key = "s4q4286j9kao"
key_secret = "7udbterlug9pds6"
headers = {
    "Accept": "application/json",
    "Content-Type": "application/json"
}

extr_dir = "extracted"
meta_dir = "meta"


def append(failed_url, file_name):
    with open(file_name, "a") as f:
        f.write(failed_url + "\n")


def load_scanned_urls():
    with open("meta/scanned.txt", "r") as annotated:
        return set([url.strip("\n\r") for url in annotated.readlines()])


def add_to_scanned_urls(new_url):
    with open("meta/scanned.txt", "a") as annotated:
        annotated.write(new_url + "\n")


def is_outer_url(url, base_url):
    if base_url in url:
        return False
    return True


def prepare_link(url, href):
    if is_erroneous_address(url) or is_erroneous_address(href):
        return url
    return urljoin(url, href)


def is_erroneous_address(address):
    if address is None or address == "":
        return True
    for char in forbidden_chars:
        if char in address:
            return True
    return False


def alert(text, turn, message):
    with open(meta_dir + "/alert.txt", "a") as f:
        f.write(turn + " " + text + " " + message + "\n\n\n")


def annotate_doc(doc):
    with open(doc, "r") as f:
        content = "".join(f.read())

    data = {
        "document": content,
        "documentType": "text/html",
    }
    jsonData = json.dumps(data)
    req = requests.post(
        endpoint, headers=headers,
        data=jsonData, auth=(api_key, key_secret))

    try:
        raw = req.content.decode("utf-8")
        return raw
    except Exception:
        print(Exception.message)
        alert(doc, Exception.message)


def process_doc(doc):
    with open(meta_dir + "/turn.txt", "r") as f:
        turn = "".join(f.read().replace("\n", ""))

    with open(extr_dir + "/" + turn + ".html", "w+") as html_file:
        html_file.write(doc)

    with open(meta_dir + "/turn.txt", "r+") as f:
        f.seek(0)
        f.write(str(int(turn) + 1))
        f.truncate()
