import random

def selectHerb():
    herbs = open("herbsList").readlines()
    print(random.choice(herbs))

if __name__ == '__main__':
    selectHerb()

