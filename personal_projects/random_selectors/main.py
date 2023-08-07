import random

def selectHerb():
    herbs = open("lol_champs_mids").readlines()
    print(random.choice(herbs))

if __name__ == '__main__':
    selectHerb()

