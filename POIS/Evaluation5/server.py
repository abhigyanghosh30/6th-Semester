import socket
import sys
import numpy as np
from sss import SSS
from random import randint
import random
from generate import generate
from sign import sign
from verify import verify
import math
from config import params

n = params['n']
g = params['g']
p = params['p']
k = params['k']
n = params['n']

x = int(open('server_privkey','r').read())
y = int(open('server_pubkey','r').read())
data = np.random.randint(100,size=k)
print(data)

s = socket.socket()
port = params['port']
s.bind(('', port))
s.listen(5)
while True:
    c, addr = s.accept()
    shares = []
    for j in range(n):
        shares.append(tuple(str(c.recv(1024),encoding='ascii').split(',')))
    print(shares)
    sss = SSS()
    i = sss.reconstruct_secret(shares)
    i = int(i)
    shares = sss.construct_shares(data[i])
    print("shares=",shares)
    try:
        for share in shares:
            sig = sign(x,share[1])
            c.send(bytes(str(share[0])+","+",".join(sig),encoding='ascii'))
    except:
        c.close()
    finally:
        c.close()
