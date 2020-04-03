import random
from generate import generate
from sign import sign
from verify import verify

print("Enter number of bits")
n = int(input())
g, p = generate(n)
x = random.randint(1,p-1) #private key
print("Private Key =",x)
y = pow(g,x,p) #public key
print("Public Key =",y)
m, z, c, t = sign(x,n,g,p) 
print(verify(m,z,c,t,n,g,p,y)) # not x
