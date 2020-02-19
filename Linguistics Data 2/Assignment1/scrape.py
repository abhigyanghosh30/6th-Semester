import requests 


payload = {'notation':'utf','out_notation':'utf','input':'जानवरों में गधा सबसे ज्यादा बुद्धिमान समझा जाता है'}

response = requests.post(
    url='http://ltrc.iiit.ac.in/analyzer/hindi/run.cgi',
    data=payload,
    headers={'content-type':'application/x-www-form-urlencoded'}
)
response = requests.get(url='http://ltrc.iiit.ac.in/analyzer/hindi/all_out')
output = response.text
