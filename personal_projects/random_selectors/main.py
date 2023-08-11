import random

def selectHerb():
    herbs = open("league/epic_items").readlines()
    print(random.choice(herbs))

if __name__ == '__main__':
    selectHerb()

