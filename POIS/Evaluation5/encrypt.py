import random
from config import params

p = params['p']
g = params['g']

def encrypt(m,h):
    y = random.randint(1,p-1)
    s = pow(h,y,p)
    c_1 = pow(g,y,p)
    c_2 = m*s%p
    return c_1,c_2
