import stanfordnlp
MODELS_DIR = '.'
stanfordnlp.download('hi', MODELS_DIR)
nlp = stanfordnlp.Pipeline(processors='tokenize,pos,depparse', models_dir=MODELS_DIR, treebank='hi_hdtb', use_gpu=True, pos_batch_size=3000)
f = open('corpus_revised_20171089.txt','r')
f2 = open('parse.txt','w')
f3 = open('hindi.output.txt','r')
lines = f.read()
doc = nlp(lines)
morph_lines = f3.readlines()
for sentence in doc.sentences:
    for word in sentence.words:
        morph = morph_lines[int(word.index)-1].split('\t')
        f2.write(str(word.index.rjust(2))+"\t"+word.text+"\t"+word.xpos+"\t")
        f2.write("<"+",".join(morph[4:-1])+">\t")
        f2.write(str(word.governor)+"\t"+word.dependency_relation+"\n")
    f2.write("\n")
f.close()
f2.close()
f3.close()

# doc.sentences[0].print_dependencies()

