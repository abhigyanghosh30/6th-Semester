import json
from collections import defaultdict

table = json.load(open('table.json','r'))
invert_table = defaultdict(list) 
for key in table:
    for elements in table[key]:
        invert_table[elements].append(key)

json.dump(invert_table,open('invert_table.json','w'))

print(invert_table)
