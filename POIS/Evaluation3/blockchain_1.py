from hash import hash

class Blockchain1:
    def __init__(self, n, g, p):
        self.n = n
        self.g = g 
        self.p = p
        self.blockchain = {}
        self.last_node = 0

    def push(self, data):
        address = self.last_node+1
        self.blockchain[address]={"prev_node":self.last_node,"data":data}
        self.last_node = address

    def pop(self):
        self.last_node = self.blockchain[self.last_node.prev_node]

    def print_stack(self):
        node = self.blockchain[self.last_node]
        print(node["data"],end="->")
        while node["prev_node"] != 0:
            node = self.blockchain[node["prev_node"]]
            print(node["data"],end="->")
        print()