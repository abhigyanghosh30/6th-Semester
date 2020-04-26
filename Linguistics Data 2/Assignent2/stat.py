from collections import defaultdict
import sqlite3
conn = sqlite3.connect('stat.db')
c = conn.cursor()
f = open('markers.txt','w+')
for row in c.execute('select * from markers order by tag,count desc;'):
    f.write(row[0] +' & '+row[1]+' & '+str(row[2])+'\\\\')
    f.write('\n\\hline\n')

# stats = defaultdict(int)

# sentences = open('20171089.txt').read().split('\n----------------------------------------\n')
# for sentence in sentences:
#     words = sentence.split('\n')[1:-1]
#     for word in words:
#         attrs = word.split('\t')
#         if(attrs[7]=='lwg__psp'):
#             head = words[int(attrs[6])-1]
#             head_attrs = head.split('\t')
#             stats[(head_attrs[7],attrs[1])]+=1

# print(stats)

# for stat in stats:
#     c.execute("INSERT INTO markers VALUES('{tag}','{marker}',{count})".format(tag=stat[0],marker=stat[1],count=stats[stat]))

conn.commit()
conn.close()