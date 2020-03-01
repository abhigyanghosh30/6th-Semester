import requests 
from bs4 import BeautifulSoup

f = open('data.txt','r')
lines = f.readlines()
for line in lines:
    print(line.strip())
    payload = {'notation':'utfoutput','out_notation':'utf','input':line.strip()}

    response = requests.post(
        url='http://ltrc.iiit.ac.in/analyzer/hindi/run.cgi',
        data=payload,
        headers={'content-type':'application/x-www-form-urlencoded'}
    )
    soup = BeautifulSoup(response.text, 'html.parser')
    outputs = soup.find_all('table')[1]
    # print(outputs)

    for lines in outputs.find_all('tr'):
        for data in lines.find_all('td'):
            for content in data.contents:
                print(content.replace(u'\xa0', u' '),end="")
        print()
