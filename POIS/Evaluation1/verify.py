from hash import hash

def verify(M,z,c,t,n,g,p,y):
    m_binary = ''.join(format(ord(i), 'b') for i in M)
    if c == hash(int(bin(t)+m_binary,2),g,p,n):
        if (pow(y,c,p)*t)%p == pow(g,z,p):
            return "Signs and message match"
        else:
            return "Signs are incorrect"
    else:
        return "Message is not the same"