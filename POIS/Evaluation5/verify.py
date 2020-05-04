from hash import hash
from config import params

def verify(M,z,c,t,y):
    g = params['g']
    p = params['p']
    n = params['b']
    m_binary = bin(int(M))[2:]
    z = int(z)
    c = int(c)
    t = int(t)
    y = int(y)

    if c == hash(int(bin(t)+m_binary,2)):
        if (pow(y,c,p)*t)%p == pow(g,z,p):
            return True
        else:
            return False
    else:
        return False