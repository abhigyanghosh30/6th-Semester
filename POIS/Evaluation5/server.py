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

private_key_file = open('server_privkey','w+')
public_key_file = open('server_pubkey','w+')
data = np.random.randint(100,size=k)
print(data)
print("Enter n")
n = int(input())


s = socket.socket()
port = 12345
s.bind(('', port))
s.listen(5)
while True:
    c, addr = s.accept()
    x = random.randint(1,p-1) #private key
    print("Private Key =",x)
    y = pow(g,x,p) #public key
    print("Public Key =",y)
    sss = SSS(data, n, p)
    print(sss.production_poly.coef)
    shares = sss.construct_shares()
    try:
        for share in shares:
            sig = sign(x,share[1],b,g,p)
            print(sig)
            c.send(bytes(",".join(sig),encoding='ascii'))
    except:
        c.close()
    finally:
        c.close()
