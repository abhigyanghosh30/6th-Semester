import stanfordnlp
MODELS_DIR = '.'
stanfordnlp.download('hi', MODELS_DIR)
nlp = stanfordnlp.Pipeline(processors='tokenize,pos,depparse', models_dir=MODELS_DIR, treebank='hi_hdtb', use_gpu=False, pos_batch_size=3000)
f = open('corpus_revised_20171089.txt','r')
f2 = open('parse.txt','w')
f3 = open('hindi.output.txt','r')
lines = f.read()
doc = nlp(lines)
for sentence in doc.sentences:
    for word in sentence.words:
        f2.write(str(word.index.rjust(2))+"\t"+word.text+"\t"+word.xpos+"\t"+str(word.governor)+"\t"+word.dependency_relation+"\n")
    f2.write("\n")
f.close()
f2.close()

# doc.sentences[0].print_dependencies()