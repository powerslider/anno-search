import os
import json


json_files = set()
errors = set()
dir = "extracted/json/"

for file in os.listdir(dir):
    if ".json" in file:
        json_files.add(file)
        with open(dir + file, "r") as f:
            j = json.loads("".join(f.read()))
            if j["entities"] == {} or j["text"] == "":
                errors.add(file)

print(errors or "All good. Scanned files: " + str(len(json_files)))
