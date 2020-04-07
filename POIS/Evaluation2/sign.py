import random
from hash import hash

def sign(x,m,n,g,p):
    # rem = n - len(m_binary)%n
    # m_binary = int(m_binary,2)
    # m_binary = m_binary<<rem
    # print(m_binary)
    m_binary = bin(int(m))[2:]
    r = random.randint(1,p-1)
    t = pow(g,r,p)
    print(bin(t)+m_binary)
    c = hash(int(bin(t)+m_binary,2),g,p,n)
    z = c * x + r
    return m, z, c, t