with open('corpus_20171089.txt') as f:
    lines = f.readlines()
    for line in lines:
        words = line.split(' ')
        for i in range(len(words)):
            print(i+1," ",words[i])        
