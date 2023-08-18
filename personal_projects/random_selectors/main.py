import random

def selectHerb():
    herbs = open("league/lol_champs_jungles").readlines()
    print(random.choice(herbs))

if __name__ == '__main__':
    selectHerb()

