import json
from collections import defaultdict

inverted_table = json.load(open('invert_table.json','r'))

data_ac = json.load(open('test_ac.json','r'))

def get_best_ud(tag):
    print(tag, end='->')
    if tag in table and len(table[tag])==1:
        print(table[tag][0])
        return table[tag][0]

converted = data_ac

for key in converted:
    for i in range(1,len(converted[key]['tags'])):
        converted[key]['tags'][i] = get_best_ac(converted[key]['tags'][i])

print(converted)
output = open('converted_ud.json','w')
json.dump(converted,output)
output.close()