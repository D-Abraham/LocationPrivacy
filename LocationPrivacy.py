import math
import csv


u1 = [6,0]
u2 = [3,-4]
users_xy = []
x,y = [],[]

#readf = open("test.txt","r").read().splitlines()

f = open("test.txt","r")
#l = f.read().split("\n")
for l in f:
    row =l.split()
    x.append(row[0])
    y.append(row[1])


"""
with open("test.txt", "r") as input:
    for line in input:
        users_xy.append(line)

for i in range (2):
    print (users_xy[i])
"""
for i in range (2):
    print (x[i]+""+y[i] )

def get_distance(x1,x2):
    print (math.sqrt((x2[0]-x1[0])**2+(x2[1]-x1[1])**2))


get_distance(u1,u2)
