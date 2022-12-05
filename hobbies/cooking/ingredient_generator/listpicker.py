import random
import os
import sys

def pickList(filename, totalPicks):
    lines = []
    with open(filename) as __file:
        line = __file.readline()
        lines.append(line)
        while line:
            line = __file.readline()
            lines.append(line)
    chosenLines = ""
    for i in range(int(totalPicks)):
        chosenLines += lines[random.randint(0, len(lines) - 1)]
    return chosenLines

if __name__ == "__main__":
    if len(sys.argv) > 1:
        filename = sys.argv[1]
    else: 
        print("missing first argument for filename") 
    if len(sys.argv) > 2:
        totalPicks = sys.argv[2]
        print(pickList(filename, totalPicks))
    else:
        print(pickList(filename, random.randint(1, 3)))

    
