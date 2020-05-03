from numpy.polynomial.polynomial import Polynomial as Poly
import numpy.polynomial.polynomial as polynomial
class SSS:
    
    def __init__(self, production_coefs, n, p):
        self.n = n
        self.k = len(production_coefs)
        self.p = p

        self.production_coefs = production_coefs
        self.production_poly = Poly(production_coefs)

    def construct_shares(self):

        return [(x, polynomial.polyval(x, self.production_poly.coef)) for x in range(1, self.n + 1)]
    
    def reconstruct_secret(self, shares):

        if len(shares) < self.k:

            raise Exception("Not enough ks")

        x = [a for a, b in shares]
        y = [b for a, b in shares]

        return polynomial.polyfit(x,y, self.k-1)
        # return SSS.L(x, y, self.k).coef[:self.k] % self.p
