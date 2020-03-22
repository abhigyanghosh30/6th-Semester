import stanfordnlp
import sys
import json 

MODELS_DIR=".."
nlp = stanfordnlp.Pipeline(processors='tokenize,pos,depparse', models_dir=MODELS_DIR, treebank='hi_hdtb', use_gpu=False, pos_batch_size=3000)
f = open(sys.argv[1],'r')
full = f.read().split('\n')
i=1
data = {}
for lines in full:
    print(lines)
    doc = nlp(lines)
    for sentence in doc.sentences:
        data[str(i)] = {"words":['<start>'],"tags":['sos']}
        for word in sentence.words:
            data[str(i)]["words"].append(word.text)
            data[str(i)]["tags"].append(word.dependency_relation)
        data[str(i)]["tags"].append('eos')
        data[str(i)]["words"].append('<end>')
    i+=1
f.close()

json_ac = open(sys.argv[2],'w')
json.dump(data,json_ac)
json_ac.close()