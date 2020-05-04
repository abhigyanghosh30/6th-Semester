import socket
from sss import SSS
from config import params
from sign import sign

s = socket.socket()
port = params['port']
s.connect(('localhost',port))

x = int(open('client_privkey','r').read())
y = int(open('client_pubkey','r').read())

sss = SSS()

print("Enter index of element to fetch")
i = int(input())
shares = sss.construct_shares(i)
print(shares)
# try:
for share in shares:
    sig = sign(x, share[1])
    s.send(bytes(str(share[0])+","+",".join(sig),encoding='ascii'))
# except:
#     s.close()

shares = []
for j in range(params['n']):
    r = s.recv(1024)
    shares.append(tuple(str(r,encoding='ascii').split(',')))
    # try: 
print(shares)
print(sss.reconstruct_secret(shares))
    # except:
    #     continue

s.close()
