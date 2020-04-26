def dtw(word):  
    vowels = {
    'अ':'a',
    'आ':'A',
    'ा':'A',
    'इ':'i',
    'ि':'i',
    'ई':'I',
    'ी':'I',
    'उ':'u',
    'ु':'u',
    'ऊ':'u',
    'ू':'u',
    'ए':'e',
    'े':'e',
    'ऐ':'E',
    'ै':'E',
    'ओ':'o',
    'ो':'o',
    'औ':'O',
    'ौ':'O',
    'ऋ':'q',
    'ृ':'q',
    '्':'',
    'ं':'M',
    'ँ':'z',
    '़':'',
    '\u200d':'',
    ' ':' ',
    }
    consonants = {
    'क':'k',
    'ख':'K',
    'ग':'g',
    'घ':'G',
    'च':'c',
    'छ':'C',
    'ज':'j',
    'झ':'J',
    'ट':'t',
    'ठ':'T',
    'ड':'d',
    'ढ':'D',
    'ण':'N',
    'त':'w',
    'थ':'W',
    'द':'x',
    'ध':'X',
    'न':'n',
    'प':'p',
    'फ':'P',
    'ब':'b',
    'भ':'B',
    'म':'m',
    'य':'y',
    'र':'r',
    'ल':'l',
    'व':'v',
    'श':'S',
    'ष':'R',
    'स':'s',
    'ह':'h',
    'ज़':'j',
    'फ़':'P',
    'क़':'k',
    'ख़':'K',
    }
    wx = ''
    for i in range(len(word)):
        if word[i] in consonants:
            wx = wx+consonants[word[i]]
            if i+1==len(word):
                wx=wx+'a'
            elif word[i+1] in consonants:
                wx=wx+'a'

        else:
            wx = wx+vowels[word[i]]
    print(word,wx)
    return wx