from hash import hash

class Blockchain:
    def __init__(self):
        self.blockchain = {}

    def add_data(self, data):
        address = hash(self.blockchain[self.get_last_index(self)])
        self.blockchain[address]=data

    def get_last_index(self):
        return self.blockchain.keys()[-1]