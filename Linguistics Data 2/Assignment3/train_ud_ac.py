import json
from collections import defaultdict

f_ac = open('abhigyan_ac.json','r')
data_ac = json.load(f_ac)

f_ud = open('UD.json','r')
data_ud = json.load(f_ud)

f_table = open('table.json','r')
table = json.load(f_table)

bigrams = defaultdict(int)
for key in data_ac:
    for i in range(1,len(data_ac[key]['tags'])):
        print(data_ac[key]['tags'][i])
        bigrams[data_ac[key]['tags'][i-1]+","+data_ac[key]['tags'][i]]+=1

converted = data_ud

def get_best_ac(tag,prev):
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


for key in converted:
    for i in range(1,len(converted[key]['tags'])):
        converted[key]['tags'][i] = get_best_ac(converted[key]['tags'][i],converted[key]['tags'][i-1])

print(converted)
output = open('abhigyan_converted_ud.json','w')
json.dump(converted,output)
output.close()