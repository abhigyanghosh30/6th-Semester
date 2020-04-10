from blockchain_1 import Blockchain1
from blockchain_2 import Blockchain2
from blockchain_3 import Blockchain3
from generate import generate
import random

n = 20
g, p  = generate(n)

x_1 = random.randint(1,p-1)
y_1 = pow(g,x_1,p)

x_2 = random.randint(1,p-1)
y_2 = pow(g,x_1,p)

blockchain1 = Blockchain1(n,g,p)
blockchain2 = Blockchain2(n,g,p)
blockchain3 = Blockchain3(n,g,p)
blockchain1.push(100)
blockchain2.push(100)
blockchain3.push(100, x_1)
blockchain1.push(200)
blockchain2.push(200)
blockchain2.push(300)
blockchain2.print_stack()
blockchain3.push(200, x_2)
blockchain2.pop()
# print(blockchain1.blockchain)
blockchain1.print_stack()
blockchain2.print_stack()
blockchain3.print_stack()
