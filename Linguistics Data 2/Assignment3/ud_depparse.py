import stanfordnlp
import sys
import json 

MODELS_DIR=".."
nlp = stanfordnlp.Pipeline(processors='tokenize,pos,depparse', models_dir=MODELS_DIR, treebank='hi_hdtb', use_gpu=False, pos_batch_size=3000)
f = open(sys.argv[1],'r')
lines = f.read()
doc = nlp(lines)

data = {}

i=1
for sentence in doc.sentences:
    data[str(i)] = {"words":['<start>'],"tags":['sos']}
    for word in sentence.words:
        data[str(i)]["tags"].append(word.text)
        data[str(i)]["words"].append(word.dependency_relation)
    data[str(i)]["tags"].append('eos')
    data[str(i)]["words"].append('<end>')
    i+=1
f.close()

json_ac = open(sys.argv[2],'w')
json.dump(data,json_ac)
json_ac.close()