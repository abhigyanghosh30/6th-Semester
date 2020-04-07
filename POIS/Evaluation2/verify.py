from hash import hash

def verify(M,z,c,t,n,g,p,y):
    m_binary = bin(int(M))[2:]
    if c == hash(int(bin(t)+m_binary,2),g,p,n):
        if (pow(y,c,p)*t)%p == pow(g,z,p):
            return True
        else:
            return False
    else:
        return False