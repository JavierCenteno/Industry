# -*- coding: utf-8 -*-
"""
This script will remove formatting from all the json data and
internationalization files to shave off some bytes off their size.

It takes files from res/data/ and res/i18n/ and saves the result in
script/res/data/ and script/res/i18n/

@author: Javier Centeno Vega
"""

import codecs
import json
import os

# location of the source directory (from the project's root folder)
# also location of the target directory (from scripts)
directories = ["res/data", "res/i18n"]

for directory in directories:
	if not (os.path.exists(directory) and os.path.isdir(directory)):
		os.makedirs(directory)
	for file in os.listdir("../" + directory):
		if file.endswith(".json"):
			source = codecs.open("../" + directory + "/" + file, "r", encoding="utf8")
			target = codecs.open(directory + "/" + file, "w", encoding="utf8")
			target.write(json.dumps(json.loads(source.read()), separators=(',', ':')))
			source.close()
			target.close()
