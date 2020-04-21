X_train = open('src-train.txt').read().split('\n')
y_train = open('tgt-train.txt').read().split('\n')

from collections import defaultdict

trigrams = defaultdict(int)
bigrams = defaultdict(int)
unigrams = defaultdict(int)

for i in range(len(X_train)):
    words = X_train[i].split(' ')
    words.append('<eos>')
    words.insert(0,'<sos>')
    tags = y_train[i].split(' ')
    a = []
    for j in range(1,len(words)-1):
        x = words[j]
        x_1 = words[j+1]
        x_2 = words[j-1]
        trigrams[x+","+x_1+","+x_2,tags[j-1]]+=1
        bigrams[x+","+x_1,tags[j-1]]+=1
        unigrams[x,tags[j-1]]+=1

def predict_tag(x,x_1,x_2):
    max = 0
    max_key = "undef"
    for key in trigrams:
        if key[0] == x+","+x_1+","+x_2 and trigrams[key] > max:
            max = trigrams[key]
            max_key = key[1]
    if max_key == "undef":
        for key in bigrams:
            if key[0] == x+","+x_1 and bigrams[key] > max:
                max = bigrams[key]
                max_key = key[1]
    if max_key == "undef":
        for key in unigrams:
            if key[0] == x and unigrams[key] > max:
                max = unigrams[key]
                max_key = key[1]
    return max_key

f = open('stat_out.txt','w')
for i in range(len(X_test)):
    words = X_test[i].split(' ')
    words.append('<eos>')
    words.insert(0,'<sos>')
    a = []
    for j in range(1,len(words)-1):
        tag = predict_tag(words[j],words[j+1],words[j-1])
        f.write(tag+" ")
        print(tag)
    f.write('\n')
f.close()