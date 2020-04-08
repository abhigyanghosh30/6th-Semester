import random
import textwrap
from generate import generate

def hash(M,g,p,n):
    # padding
    M = bin(M)[2:]
    rem = n - len(M)%n
    blocks = textwrap.wrap(M[2:],n)
    h = (1<<n) - p
    z = p-1
    for i in range(len(blocks)):
        z = (pow(g,z,p)*pow(h,int(blocks[i],2)))%p  
    return z