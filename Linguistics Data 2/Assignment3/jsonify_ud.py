import sys
import json
from collections import defaultdict

f_ac = open(sys.argv[1],'r')

data = {}
data_ac = f_ac.read().split('----------------------------------------\n')
for i in range(len(data_ac)):
    print(data_ac[i])
    words = data_ac[i].split('\n')
    words.pop()
    data[str(i)] = {"words":['start'],"tags":['sos']}
    for word in words:
        attrs = word.split('\t')
        data[str(i)]["tags"].append(attrs[5])
        data[str(i)]["words"].append(attrs[1])
    data[str(i)]["tags"].append('eos')
    data[str(i)]["words"].append('end')
f_ac.close()

json_ac = open(sys.argv[2],'w')
json.dump(data,json_ac)
json_ac.close()