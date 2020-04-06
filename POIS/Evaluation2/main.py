import numpy as np
from sss import SSS
from random import randint
from generate import generate

print("Enter b")
b = int(input())
print("Enter k")
k = int(input())
data = np.random.randint(100,size=k)
print(data)
print("Enter n")
n = int(input())

# print("Enter e")
# e = int(input())

# b = 10
# k = 5
# data = [5, 2, 3, 1, 2]
# n = 10

g, p = generate(b)
sss = SSS(data, n, p)
print(sss.production_poly.coef)
shares = sss.construct_shares()
print(shares)
print(sss.reconstruct_secret(shares))