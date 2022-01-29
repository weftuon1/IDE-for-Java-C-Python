from math import sqrt

Prime = []

def is_Prime(num):
    p = True
    for i in Prime:
        if i <= sqrt(num):
            if num % i == 0:
                p = False
                break
        else:
            break
    return p

t = int(input("Please enter the range: "))

for i in range(2, t+1):
    if is_Prime(i):
        Prime.append(i)
        
print(Prime)
    
