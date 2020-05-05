import socket
import random
import numpy as np
from decrypt import decrypt
import math
from config import params

n = params['n']
g = params['g']
p = params['p']
k = params['k']
n = params['n']

x = int(open('server_privkey','r').read())
data = np.random.randint(100,size=k)
print("Data array in server",data)

s = socket.socket()
port = params['port']
s.bind(('', port))
s.listen(5)

while True:
    # try:
    c, addr = s.accept()
    R = str(c.recv(1024),encoding='ascii').split(';')
    print("Encrypted array from client",R)
    D = []
    for r in R:
        c_1 = int(r.split(',')[0])
        c_2 = int(r.split(',')[1])
        D.append(decrypt(c_1,c_2,x))
    print("Decrypted array",D)
    DB = []
    for i in range(k):
        DB.append(str(D[i]^data[i]))

    print("Array sent by server",DB)
    c.send(bytes(",".join(DB),encoding='ascii'))
    # except:
    #     c.close()

    # finally:
    #     c.close()
