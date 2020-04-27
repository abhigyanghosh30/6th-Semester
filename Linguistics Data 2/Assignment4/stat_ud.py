from collections import defaultdict
from devnagri_to_wx import dtw
import sqlite3
conn = sqlite3.connect('UD.db')
c = conn.cursor()

# tags = defaultdict(int)
# markers = defaultdict(int)

# c.execute('CREATE TABLE tags (tag TEXT, count INTEGER)')
# c.execute('CREATE TABLE markers (tag TEXT, marker TEXT, count INTEGER)')

# sentences = open('hi_hdtb-ud-train.conllu.txt').read().split('\n\n')
# for sentence in sentences:
#     words = sentence.split('\n')[2:]
#     for i in range(len(words)):
#         attrs = words[i].split('\t')
#         tags[attrs[7]]+=1
#         if attrs[4] == 'PSP':
#             next_word = words[i+1].split('\t')
#             head = words[int(attrs[6])-1]
#             head_attrs = head.split('\t')
#             if next_word[4] == 'PSP' and next_word[6] == attrs[6]: 
#                 markers[(attrs[1]+" "+next_word[1],head_attrs[7])]+=1
#             else:
#                 markers[(attrs[1],head_attrs[7])]+=1


# for tag in tags:
#     c.execute("INSERT INTO tags VALUES ('{tag}',{count})".format(tag=tag,count=tags[tag]))

# for marker in markers:
#     c.execute("INSERT INTO markers VALUES ('{tag}','{marker}',{count})".format(tag=marker[0],marker=marker[1],count=markers[marker]))

# f = open('tags_markers_ud.txt','w+')

# for row in c.execute("select * from markers where count > 500 order by tag, count desc;"):
#     dtw(row[0])
#     f.write(row[1]+' & '+dtw(row[0])+' & '+str(row[2])+"\\\\\n\\hline\n")
# f.close()

# f = open('tags_ud.txt','w+')

# for row in c.execute("select * from tags order by count desc;"):
#     f.write(row[0]+' & '+str(row[1])+"\\\\\n\\hline\n")
# f.close()

# def n_grams(k):
#     ngrams = defaultdict(int)
#     sentences = open('hi_hdtb-ud-train.conllu.txt').read().split('\n\n')
#     for sentence in sentences:
#         words = sentence.split('\n')[2:]
#         for i in range(len(words)-k):
#             key=[]
#             for j in range(k):
#                 attrs = words[i+j].split('\t')
#                 key.append(attrs[7])
#             ngrams[tuple(key)] += 1
#     return ngrams
#     ngrams = defaultdict(int)
#     sentences = open('hi_hdtb-ud-train.conllu.txt').read().split('\n\n')
#     for sentence in sentences:
#         words = sentence.split('\n')[2:]
#         for i in range(len(words)-k):
#             key=[]
#             for j in range(k):
#                 attrs = words[i+j].split('\t')
#                 key.append(attrs[7])
#             ngrams[tuple(key)] += 1
#     return ngrams

# # c.execute('CREATE TABLE bigrams (tag1 TEXT,tag2 TEXT,count INTEGER)')
# bigrams = n_grams(2)
# for k in bigrams:
#     c.execute('INSERT INTO bigrams VALUES ("{tag1}","{tag2}",{count})'.format(tag1=k[0],tag2=k[1],count=bigrams[k]))

# c.execute('CREATE TABLE trigrams (tag1 TEXT,tag2 TEXT,tag3 TEXT,count INTEGER)')
# trigrams = n_grams(3)
# for k in trigrams:
#     c.execute('INSERT INTO trigrams VALUES ("{tag1}","{tag2}","{tag3}",{count})'.format(tag1=k[0],tag2=k[1],tag3=k[2],count=trigrams[k]))

# c.execute('CREATE TABLE tetragrams (tag1 TEXT,tag2 TEXT,tag3 TEXT,tag4 TEXT,count INTEGER)')
# tetragrams = n_grams(4)
# for k in tetragrams:
#     c.execute('INSERT INTO tetragrams VALUES ("{tag1}","{tag2}","{tag3}","{tag4}",{count})'.format(tag1=k[0],tag2=k[1],tag3=k[2],tag4=k[3],count=tetragrams[k]))

f = open('tetragrams_ud.txt','w+')
for row in c.execute('select * from tetragrams order by count desc LIMIT 15;'):
    f.write(row[0]+' & '+row[1]+' & '+row[2]+' & '+row[3]+' & '+str(row[4])+"\\\\\n\\hline\n")
f.close()

conn.commit()
conn.close()