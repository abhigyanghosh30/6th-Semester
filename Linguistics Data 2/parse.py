import stanfordnlp
MODELS_DIR = '.'
stanfordnlp.download('hi', MODELS_DIR)
nlp = stanfordnlp.Pipeline(processors='tokenize,pos,depparse', models_dir=MODELS_DIR, treebank='hi_hdtb', use_gpu=True, pos_batch_size=3000)
f = open('corpus_revised_20171089.txt','r')
f2 = open('parse.txt','w')
f3 = open('hindi.output.txt','r')
tags = {}
lines = f.read()
doc = nlp(lines)
morph_lines = f3.readline()
for sentence in doc.sentences:
    for word in sentence.words:
        morph = morph_lines.split('\t')
        f2.write(str(word.index.rjust(2))+"\t"+word.text+"\t"+word.xpos+"\t")
        f2.write("<"+",".join(morph[4:-1])+">\t")
        f2.write(str(word.governor)+"\t"+word.dependency_relation+"\n")
        if word.dependency_relation in tags:
            tags[word.dependency_relation]+=1
        else:
            tags[word.dependency_relation]=1
        morph_lines = f3.readline()
    f2.write("\n")
f.close()
f2.close()
f3.close()

new_dict = {key: value for key,value in sorted(tags.items(),key=lambda item:item[1])}

f4 = open('stat.txt','w')
for tag in new_dict:
    f4.write(tag+":"+str(new_dict[tag])+"\n")
f4.close
# doc.sentences[0].print_dependencies()

