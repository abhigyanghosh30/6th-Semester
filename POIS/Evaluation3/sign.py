import random
from hash import hash

def sign(x,m,n,g,p):
    m_binary = bin(int(m))[2:]
    r = random.randint(1,p-1)
    t = pow(g,r,p)
    c = hash(int(bin(t)+m_binary,2),g,p,n)
    z = c * x + r
    return m, z, c, t