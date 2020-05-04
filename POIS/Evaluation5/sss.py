from numpy.polynomial.polynomial import Polynomial as Poly
import numpy.polynomial.polynomial as polynomial
import random
from config import params
class SSS:
    
    def __init__(self):
        self.n = params['n']
        self.k = params['k']


    def construct_shares(self, production_coefs):
        production_poly = Poly(production_coefs)
        return [(x, polynomial.polyval(x, production_poly.coef)) for x in range(1, self.n + 1)]
    
    def reconstruct_secret(self, shares):

        if len(shares) < self.k:

            raise Exception("Not enough ks")

        x = [int(share[0]) for share in shares]
        y = [int(share[1]) for share in shares]
        return  polynomial.polyfit(x,y, self.k-1)
        # return SSS.L(x, y, self.k).coef[:self.k] % self.p
