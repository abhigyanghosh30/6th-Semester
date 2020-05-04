import random
import textwrap
from generate import generate
from config import params

def hash(M):
    # padding
    g = params['g']
    p = params['p']
    n = params['b']
    M = bin(M)[2:]
    rem = n - len(M)%n
    # M = int(M,2)
    # M = M<<rem
    # M = bin(M)
    blocks = textwrap.wrap(M[2:],n)
    h = (1<<n) - p
    z = p-1
    for i in range(len(blocks)):
        z = (pow(g,z,p)*pow(h,int(blocks[i],2)))%p  
    return z      


