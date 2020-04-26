from collections import defaultdict
from devnagri_to_wx import dtw
import sqlite3
conn = sqlite3.connect('AnnCorra.db')
c = conn.cursor()

tags = defaultdict(int)
markers = defaultdict(int)

# sentences = open('train.txt').read().split('\n\n')
# for sentence in sentences:
#     words = sentence.split('\n')
#     for i in range(len(words)):
#         attrs = words[i].split('\t')
#         tags[attrs[7]]+=1
#         if attrs[7] == 'lwg__psp':
#             next_word = words[i+1].split('\t')
#             head = words[int(attrs[6])-1]
#             head_attrs = head.split('\t')
#             if next_word[7] == 'lwg__psp' and next_word[6] == attrs[6]: 
#                 markers[(attrs[1]+" "+next_word[1],head_attrs[7])]+=1
#             else:
#                 markers[(attrs[1],head_attrs[7])]+=1


# for tag in tags:
#     c.execute("INSERT INTO tags VALUES ('{tag}',{count})".format(tag=tag,count=tags[tag]))

# for marker in markers:
#     c.execute("INSERT INTO markers VALUES ('{tag}','{marker}',{count})".format(tag=marker[0],marker=marker[1],count=markers[marker]))

# f = open('marker_tags_anncorra.txt','w+')

# for row in c.execute("select * from markers where count > 500 order by marker, count desc;"):
#     dtw(row[0])
#     f.write(row[1]+' & '+dtw(row[0])+' & '+str(row[2])+"\\\\\n\\hline\n")
# f.close()

f = open('tags_anncorra.txt','w+')

for row in c.execute("select * from tags order by count desc;"):
    f.write(row[0]+' & '+str(row[1])+"\\\\\n\\hline\n")
f.close()

conn.commit()
conn.close()