import random

def selectHerb():
    herbs = open("ios_frameworks").readlines()
    print(random.choice(herbs))

if __name__ == '__main__':
    selectHerb()

