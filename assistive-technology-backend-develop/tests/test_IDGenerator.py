from ..objects.IDGenerator import IDGenerator

idGen = IDGenerator()

l = []
for i in range(100):
    l.append(idGen.generateID())

while len(l) > 0:
    i = l.pop()
    for  j in l:
        if j == i:
            print("fail")

