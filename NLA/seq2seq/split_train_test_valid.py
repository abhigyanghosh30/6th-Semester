import sys
f = open(sys.argv[1],'r')
data = f.read().split('\n')

train_len = int(len(data)*10/15.7)
test_len = int(len(data)*2.7/15.7)
valid_len = int(len(data)*3/15.7)

train = open(sys.argv[1]+'-train.txt','w+')
test = open(sys.argv[1]+'-test.txt','w+')
valid = open(sys.argv[1]+'-valid.txt','w+')

for line in data[:train_len]:
    train.write(line)
    train.write('\n')

for line in data[train_len:train_len+test_len]:
    test.write(line)
    test.write('\n')

for line in data[train_len+test_len:]:
    valid.write(line)
    valid.write('\n')
