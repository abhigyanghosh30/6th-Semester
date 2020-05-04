from config import params

p = params['p']
g = params['g']

def decrypt(c_1,c_2,x):
    s = pow(c_1,x,p)
    sinv = pow(s,p-2,p)
    m = c_2*sinv%p
    return m