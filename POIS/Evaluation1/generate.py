import random
import math

def miillerTest(d, n): 
      
    a = 2 + random.randint(1, n - 4); 
  
    x = pow(a, d, n); 
  
    if (x == 1 or x == n - 1): 
        return True; 
  
    while (d != n - 1): 
        x = (x * x) % n; 
        d *= 2; 
  
        if (x == 1): 
            return False; 
        if (x == n - 1): 
            return True; 
    return False; 

def isPrime( n, k): 
      
    if (n <= 1 or n == 4): 
        return False; 
    if (n <= 3): 
        return True; 
  
    d = n - 1; 
    while (d % 2 == 0): 
        d //= 2; 
  
    # Iterate given nber of 'k' times 
    for i in range(k): 
        if (miillerTest(d, n) == False): 
            return False; 
  
    return True

def generate(n):
    k = math.floor(math.sqrt(n/2))
    q = random.getrandbits(n)
    if isPrime(q,k):
        p = 2*q+1
        if isPrime(p,k):
            g = random.randint(1,p-1)
            if pow(g,q,p) != 1 and pow(g,2,p) != 1 :
                return g,p
            else:
                return generate(n)
        else:
            return generate(n)
    else:
        return generate(n)