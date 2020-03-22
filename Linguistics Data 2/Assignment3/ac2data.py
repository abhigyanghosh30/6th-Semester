import json

test_ac = json.load(open('test_ac.json'))
f_data = open('data.txt','w')

for i in test_ac:
    for word in test_ac[i]['words'][1:-1]:
        f_data.write(word)
        f_data.write(' ')
    f_data.write('\n')
