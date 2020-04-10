from hash import hash
from sign import sign

class Blockchain3:
    def __init__(self, n, g, p):
        self.n = n
        self.g = g 
        self.p = p
        self.blockchain = {}
        self.last_node = 0

    def push(self, data, x):
        print("Push", data)
        address = self.last_node+1
        node_hash = hash(self.get_last_data(), self.g, self.p, self.n)
        node_sign = sign(x, data, self.n, self.g, self.p)
        self.blockchain[address]={"prev_node":self.last_node,"data":data, "node_hash":node_hash, "node_sign":node_sign}
        self.last_node = address

    def get_last_index(self):
        keys = list(self.blockchain.keys())
        if len(keys) == 0:
            return 0
        else:
            return keys[-1]
    
    def get_last_data(self):
        keys = list(self.blockchain.keys())
        # genesis node
        if len(keys) == 0:
            return 0
        else:
            return self.blockchain[self.last_node]["data"]
    
    def print_stack(self):
        node = self.blockchain[self.last_node]
        print(node["data"],end="->")
        while node["prev_node"] != 0:
            node = self.blockchain[node["prev_node"]]
            print(node["data"],end="->")
        print()