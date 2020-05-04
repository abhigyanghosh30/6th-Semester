import random
from hash import hash
from config import params
import math

def sign(x,m):
    g = params['g']
    p = params['p']
    n = params['b']
    # rem = n - len(m_binary)%n
    # m_binary = int(m_binary,2)
    # m_binary = m_binary<<rem
    m_binary = bin(int(m)).split('b')[1]
    r = random.randint(1,p-1)
    t = pow(g,r,p)
    concat = bin(t)+m_binary
    c = hash(int(concat[2:],2))
    z = c * x + r
    return str(math.floor(m)), str(z), str(c), str(t)