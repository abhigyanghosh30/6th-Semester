import socket
from config import params
from encrypt import encrypt
from verify import verify
import random

s = socket.socket()
port = params['port']
k = params['k']
n = params['n']
s.connect(('localhost',port))

y = int(open('server_pubkey','r').read())

print("Enter index of element to fetch")
i = int(input())

R = []
E = []
for j in range(k):
    r = random.randint(0,n)
    R.append(r)
    c_1,c_2 = encrypt(r,y)
    E.append(str(c_1)+","+str(c_2))
print("Random array",R)
print("Encrypted array sent to server",E)
s.send(bytes(";".join(E),encoding='ascii'))
DB = str(s.recv(1024),encoding='ascii').split(',')
print("Array recieved from server",DB)
print("Final data at i",R[i]^int(DB[i]))

s.close()
