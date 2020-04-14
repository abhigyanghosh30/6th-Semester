import json
import sys

data = json.load(open(sys.argv[1]))
for sent in data:
    for word in range(len(data[sent]['words'])):
        print(data[sent]['words'][word],"-->",data[sent]['tags'][word])
    print("\n")