import socket
from sss import SSS
from config import params
from sign import sign
from verify import verify

s = socket.socket()
port = params['port']
s.connect(('localhost',port))

x = int(open('client_privkey','r').read())
y = int(open('server_pubkey','r').read())

sss = SSS()

print("Enter index of element to fetch")
i = int(input())
shares = sss.construct_shares(i)
print("Shares of index i",shares)
# try:
for share in shares:
    sig = sign(x, share[1])
    s.send(bytes(str(share[0])+","+",".join(sig),encoding='ascii'))
# except:
#     s.close()

shares = []
for j in range(params['n']):
    share = tuple(str(s.recv(1024),encoding='ascii').split(','))
    if verify(*share[1:],y):
        shares.append(share)
    # try: 
print("Shares of data[i] from server",shares)
print("Reconstructed data=",sss.reconstruct_secret(shares))
    # except:
    #     continue

s.close()
