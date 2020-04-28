X_test = open('src-test.txt').read().split('\n')
y_test = open('tgt-test.txt').read().split('\n')

out = open('stat_out.txt').read().split('\n')
error = 0
total = 0
for i in range(len(y_test)):
    print(i)
    out_tags = out[i].split(' ')
    target_tags = y_test[i].split(' ')
    for j in range(len(target_tags)):
        if(out_tags[j]!=target_tags[j]):
            error+=1
            print(out_tags[j],target_tags[j])
            print(X_test[i].split(' ')[j])
        total+=1
print("Error",error)
print("Total",total)
print("Accuracy",(total-error)/total*100)