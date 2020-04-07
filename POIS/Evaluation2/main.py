import numpy as np
from sss import SSS
from random import randint
import random
from generate import generate
from sign import sign
from verify import verify

print("Enter b")
b = int(input())
print("Enter k")
k = int(input())
print("\nData:")
data = np.random.randint(100,size=k)
print(data)
print("Enter n")
n = int(input())

g, p = generate(b)
x = random.randint(1,p-1) #private key
print("Private Key =",x)
y = pow(g,x,p) #public key
print("Public Key =",y)
sss = SSS(data, n, p)
print(sss.production_poly.coef)
shares = sss.construct_shares()

print("\nShares:")
print(shares)
signs = []
for share in shares:
    print(sign(x,share[1],b,g,p))
    signs.append(sign(x,share[1],b,g,p))

print("\nSigns:")
print(signs)

valid_shares = []
i=1
for sign in signs:
    if verify(sign[0], sign[1],sign[2],sign[3],b,g,p,y):
        valid_shares.append((i, sign[0]))
    else:
        raise Exception("Corruption in share",sign)
    i+=1 
    
print("Reconstructed Data")
print(sss.reconstruct_secret(valid_shares))