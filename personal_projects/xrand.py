import random

options = ["xv", "ash", "pp", "hy"]

weights = [115, 228, 323, 385]

iterations = random.randint(1,11)


for x in range (0, iterations):
    roll = random.randint(1, 385)

    index = 0
    for weight in weights:
        if roll <= weight:
            choice = options[index]
            if choice == "xv":
                print(choice, random.randint(1,20000), random.randint(1,28))
            elif choice == "ash":
                print(choice, random.randint(1,3623), random.randint(1,300))
            elif choice == "pp":
                total = random.randint(1, 9)
                print(choice, "total: ", 9)
            elif choice == "hy":
                print(choice, "tr", random.randint(1,397), random.randint(1,20))
            break
        index += 1

