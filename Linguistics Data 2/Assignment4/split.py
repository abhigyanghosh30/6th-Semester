import sys

f = open(sys.argv[1])

src = open('src-'+sys.argv[1],'w+')
tgt = open('tgt-'+sys.argv[1],'w+')

data = f.read().split('\n\n')
for sentence in data:
    # print(sentence)
    for line in sentence.split('\n'):
        # print(line)
        attrs=line.split('\t')
        src.write(attrs[1]+" ")
        tgt.write(attrs[7]+" ")
        # print(attrs[1],end=" ")
    # print()
    src.write('\n')
    tgt.write('\n')

f.close()
src.close()
tgt.close()