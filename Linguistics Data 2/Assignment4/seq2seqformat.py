import sys

f = open(sys.argv[1])

src = open('data-'+sys.argv[1],'w+')

data = f.read().split('\n\n')
for sentence in data:

    for line in sentence.split('\n'):
        attrs=line.split('\t')
        src.write(attrs[1]+" ")
    src.write('\t')

    for line in sentence.split('\n'):
        attrs=line.split('\t')
        src.write(attrs[7]+" ")
    src.write('\n')

f.close()
src.close()