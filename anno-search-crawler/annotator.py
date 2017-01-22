import os
import utils
import json
import time


htmls = "extracted/raw/"
jsons = "extracted/json/"
txts = "extracted/txt/"

success = "extracted/Success/"
failure = "extracted/Failed/"

for file in os.listdir(htmls):
    print(file)
    file_name = os.path.splitext(file)[0]

    raw = utils.annotate_doc(htmls + file)
    try:
        struct = json.loads(raw)
        if struct["entities"] != {}:
            with open(jsons + file_name + ".json", "w+") as json_file:
                json_file.write(raw)
            with open(txts + file_name + ".txt", "w+") as text_file:
                text_file.write(struct["text"])
            print("File " + file + " annotated successfully.")
            os.rename(htmls + file, success + file)
        else:
            print("Failed to annotate file " + file + " . Please investigate.")
            print(raw)
            os.rename(htmls + file, failure + file)
        time.sleep(60)
    except:
        print("Failed to annotate file " + file + " . Please investigate.")
        print(raw)
        os.rename(htmls + file, failure + file)
