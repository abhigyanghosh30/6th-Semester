from torch import Tensor
import torch
from torch import nn
from torch import optim
import torch.nn.functional as F
from torchvision import datasets, transforms, models
from torch.autograd import Variable
import torch.optim as optim
import numpy as np

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
print(device)

X_train = open('src-train.txt').read().split('\n')
X_test = open('src-test.txt').read().split('\n')
y_train = open('tgt-train.txt').read().split('\n')
y_test = open('tgt-test.txt').read().split('\n')

tokens = set([])
for sentence in X_train+X_test:
    for word in sentence.split(' '):
        tokens.add(word)
tokens = list(tokens)
tokens.append('<sos>')
tokens.append('<eos>')
num_tokens = len(tokens)
print(num_tokens)

tag_list = set([])
for sentence in y_train+y_test:
    for tag in sentence.split(' '):
        tag_list.add(tag)
tag_list = list(tag_list)
num_tags = len(tag_list)
print(num_tags)    
    
def get_one_hot(word):
    one_hot = torch.zeros(num_tokens)
    one_hot[tokens.index(word)] = 1
    return one_hot

def get_tag(tag):
    return tag_list.index(tag)

class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.fc1 = nn.Linear(num_tokens, 1000)
        self.fc2 = nn.Linear(3 * num_tags, num_tags)
        self.fc3 = nn.Linear(1000,num_tags)
    
    def forward(self,x,x_1,x_2):
        x = Variable(x.T)
        x = self.fc1(x)
        x = F.relu(x)
        x = F.relu(self.fc3(x))
        
        x_1 = Variable(x_1.T)
        x_1 = self.fc1(x_1)
        x_1 = F.relu(x_1)
        x_1 = F.relu(self.fc3(x_1))
        
        x_2 = Variable(x_2.T)
        x_2 = self.fc1(x_2)
        x_2 = F.relu(x_2)
        x_2 = F.relu(self.fc3(x_2))
        
        x_com = torch.cat((x, x_1, x_2), 0)
        final = F.softmax(self.fc2(x_com))
        return final
        
net = Net()
net = net.float()
net.to(device)

criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(net.parameters())

for epoch in range(10):
    running_loss = 0.0
    for i in range(len(X_train)):
#         try:
        print(i, end=" ")
        optimizer.zero_grad()
        words = X_train[i].split(' ')
        words.append('<eos>')
        words.insert(0,'<sos>')
        tags = y_train[i].split(' ')
        a = []
        for j in range(1,len(words)-1):
            x = get_one_hot(words[j]).to(device)
            x_1 = get_one_hot(words[j+1]).to(device)
            x_2 = get_one_hot(words[j-1]).to(device)
            a.append(net(x,x_1,x_2))
        out = torch.stack(a)
        out.to(device)
        b = [get_tag(tag) for tag in tags]
        res = torch.tensor(b)
        res = res.cuda()
        loss = torch.tensor(0)
        loss.to(device)
        loss = criterion(out,res)
        loss.to(device)

        print(running_loss)
        loss.backward()
        optimizer.step()
        running_loss += loss.item()
#         except:
#             continue
print('Finished Training')

def predict_tag(probs):
    values, indices = torch.max(probs,0)
    return tag_list[indices]

f = open('out.txt','w')
with torch.no_grad():
    for i in range(len(X_test)):
        words = X_test[i].split(' ')
        words.append('<eos>')
        words.insert(0,'<sos>')
        tags = y_test[i].split(' ')
        a = []
        for j in range(1,len(words)-1):
            x = get_one_hot(words[j]).to(device)
            x_1 = get_one_hot(words[j+1]).to(device)
            x_2 = get_one_hot(words[j-1]).to(device)
            a.append(net(x,x_1,x_2))        
        for j in range(len(a)):
            f.write(predict_tag(a[j])+" ")
        f.write("\n")
f.close()

out = open('out.txt').read().split('\n')
error = 0
total = 0
for i in range(len(y_test)):
    print(i)
    out_tags = out[i].split(' ')
    target_tags = y_test[i].split(' ')
    for j in range(len(target_tags)):
        print(out_tags[j],target_tags[j])
        if(out_tags[j]!=target_tags[j]):
            error+=1
        total+=1
print(error)
print(total)
print((total-error)/total)