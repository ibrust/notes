import random

def selectHerb():
    herbs = open("league/mythic_items").readlines()
    print(random.choice(herbs))

if __name__ == '__main__':
    selectHerb()

