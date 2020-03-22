import json
original = json.load(open('test_ac.json','r'))
converted = json.load(open('converted_ac.json','r'))

errors = 0
total = 0 

for i in original:
    if len(original[i]['tags']) != len(converted[str(int(i)+1)]['tags']):
        continue
    print(str(int(i)+1))
    print(original[i]['tags'],end=",")
    print(converted[str(int(i)+1)]['tags'])
    for j in range(len(original[i]['tags'])):
        # print(converted[str(int(i)+1)]['tags'][j])
        if original[str(i)]['tags'][j] != converted[str(int(i)+1)]['tags'][j]:
            errors += 1
    total += 1

print(errors)
print(total)