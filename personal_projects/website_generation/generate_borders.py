import random

#border-width section
roll = random.randint(1, 10)                    #uniform border
if (roll <= 2):
    if (roll == 1):
        border_width = "medium"
    else:
        border_width = "thin"
else:
    roll = random.randint(1, 4)
    integers = [1, 5, 12, 29, 80]
    border_width = random.randint(integers[roll - 1], integers[roll])
    
top, right, bottom, left = border_width, border_width, border_width, border_width
roll = random.randint(1, 100)
matrix = [top, left, bottom, right]
if (roll <= 5):                                 #scramble the borders if necessary
    roll = random.randint(1, 3)
    for x in range(roll):
        chosen_index = random.randint(0, 3)
        second_roll = random.randint(1, 10)
        if (second_roll <= 2):
            if (second_roll == 1):
                matrix[chosen_index] = "medium"
            else:
                matrix[chosen_index] = "thin"
        else:
            roll = random.randint(1, 4)
            integers = [1, 5, 12, 29, 80]
            border_width = random.randint(integers[roll - 1], integers[roll])

top, right, bottom, left = matrix[0], matrix[1], matrix[2], matrix[3]
units = []
for x in range(4):
    if type(matrix[x]) == type("string"):
        units.append('') 
    else:
        units.append('px')
        
print("border-width: ", top, units[0], " ", right, units[1], " ", bottom, units[2], " ", left, units[3], ";", sep='')


#border-style section
units = ['none', 'hidden', 'dotted', 'dashed', 'solid', 'double', 'groove', 'ridge', 'inset', 'outset']
border_style = units[random.randint(0, len(units) - 1)]
top, right, bottom, left = border_style, border_style, border_style, border_style
roll = random.randint(1, 100)
if (roll <= 5):
    roll = random.randint(1, 3)
    matrix = [top, right, bottom, left]
    for x in range(roll):
        chosen_index = random.randint(0, 3)
        matrix[chosen_index] = units[random.randint(0, len(units) - 1)]
    top, right, bottom, left = matrix[0], matrix[1], matrix[2], matrix[3]
    
print("border-style: ", top, " ", right, " ", bottom, " ", left, ";", sep='')



#border-color section
    #I'm only having 1 here, I don't feel the need at the moment to do different border colors. 2 colors got 1 percent chance anyway, and 3-4 were substantially lower, so...
    #I can finish this later if I feel the need ... probably I will actually shade them if I ever want to use that feature... it goes up to 3% chance if I shade them (the 2 edges)
red = random.randint(0, 255)
green = random.randint(0, 255)
blue = random.randint(0, 255)
print("border-color: rgb(", red, " ", green, " ", blue, ");", sep='')



#border-radius section 

roll = random.randint(1, 100)       #determine whether to round corners in any capacity
if (roll <= 99):                    
    roll = random.randint(1, 100)
    if (roll <= 96):                #all corners are the same radii
        roll = random.randint(1, 100)
        if (roll <= 95):
            radius_A = random.randint(2, 18)
        else:
            radius_A = random.randint(2, 44)
        roll = random.randint(1, 100)
        if (roll <= 7):
            roll = random.randint(1, 100)
            if (roll <= 95):
                radius_B = random.randint(2, 18)
            else:
                radius_B = random.randint(2, 44)
            print("border-radius: ", radius_A, "px/", radius_B, "px;", sep='')
        else:
            print("border-radius: ", radius_A, "px;", sep='')
    #else:                           #2 or more corners are different radii
    






