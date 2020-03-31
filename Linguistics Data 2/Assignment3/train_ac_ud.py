import json
from collections import defaultdict

table = json.load(open('invert_table.json','r'))

data_ac = json.load(open('test_ac.json','r'))

data_ud = json.load(open('train_ud.json','r'))

bigrams = defaultdict(int)
for key in data_ud:
    for i in range(1,len(data_ud[key]['tags'])):
        print(data_ud[key]['tags'][i])
        bigrams[data_ud[key]['tags'][i-1]+","+data_ud[key]['tags'][i]]+=1

def get_best_ud(tag,prev):
    print(tag, end='->')
    if tag in table and len(table[tag])==1:
        print(table[tag][0])
        return table[tag][0]
    elif tag in table and len(table[tag]) > 1:
        max = 0
        max_tag = ''
        for key in bigrams:
            bigram = key.split(',')
            if prev==bigram[0] and bigrams[key]>max and (bigram[1] in table[tag]):
                max = bigrams[key]
                max_tag = bigram[1]
        if max == 0 and tag in table:
            max_tag = table[tag][0]
        print(max_tag)
        return max_tag
    else:
        max = 0
        max_tag = ''
        for key in bigrams:
            bigram = key.split(',')
            if prev==bigram[0] and bigrams[key]>max:
                max = bigrams[key]
                max_tag = bigram[1]
        print(max_tag)
        return max_tag

converted = data_ac

for key in converted:
    for i in range(1,len(converted[key]['tags'])):
        converted[key]['tags'][i] = get_best_ud(converted[key]['tags'][i],converted[key]['tags'][i-1])


print(converted)
output = open('converted_ud.json','w')
json.dump(converted,output)
output.close()