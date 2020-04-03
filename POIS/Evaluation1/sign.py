import random
from hash import hash

def sign(x,n,g,p):
    print("Enter a message")
    message = input() 
    m_binary = ''.join(format(ord(i), 'b') for i in message)
    # rem = n - len(m_binary)%n
    # m_binary = int(m_binary,2)
    # m_binary = m_binary<<rem
    # print(m_binary)
    r = random.randint(1,p-1)
    t = pow(g,r,p)
    print(bin(t)+m_binary)
    c = hash(int(bin(t)+m_binary,2),g,p,n)
    z = c * x + r
    return message, z, c, t