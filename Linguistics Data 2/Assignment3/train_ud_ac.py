import json

f_ac = open('abhigyan.json','r')
data_ac = json.load(f_ac)

f_ud = open('UD.json','r')
data_ud = json.load(f_ud)

f_table = open('table.json','r')
table = json.load(f_table)