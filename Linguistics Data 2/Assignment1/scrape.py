import requests 
from bs4 import BeautifulSoup

payload = {'notation':'utf','out_notation':'utf','input':'जानवरों में गधा सबसे ज्यादा बुद्धिमान समझा जाता है'}

response = requests.post(
    url='http://ltrc.iiit.ac.in/analyzer/hindi/run.cgi',
    data=payload,
    headers={'content-type':'application/x-www-form-urlencoded'}
)
soup = BeautifulSoup(response.text, 'html.parser')
outputs = soup.find_all('table')[1]
print(outputs)

for output in outputs.find_all('tr'):
    if output.get('align') == 'left':
        for content in output.contents:
            print(content.replace(u'\xa0', u' '))
