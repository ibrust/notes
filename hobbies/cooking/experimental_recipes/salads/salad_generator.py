import random 

greens = ["romaine lettuce", "watercress", "swiss chard", "spinach", "endive", "kale", "arugula", "butter leaf lettuce", "iceberg lettuce", "red leaf lettuce", "green leaf lettuce", "bok choy"]
vegetables = ["carrots", "celery", "parsnips", "cucumber", "tomato", "brocolli", "caulifflower", "bell pepper", "purple onion", "radish", "asparagus", "summer squash", "jicama", "avocado", "peas", "snow peas", "snap peas", "mushrooms", "bbrussel sprouts", "cabbage", "red cabbage", "beets"]
fruit = ["berries", "orange", "grapefruit", "peaches", "plums", "nectarines", "cherries", "melons", "pineapple", "apples", "pears", "kiwi"]
protein = ["kidney beans", "black beans", "chick peas", "chicken", "fish", "steak", "eggs", "shrimp"]
grains = ["farro", "brown rice", "wild rice", "quinoa", "barley", "corn"]
dressing = ["salsa", "guacamole", "lemon juice", "hummus", "pesto", "tzatziki", "black bean dip", "chutney"]
extras = ["raisins", "pomegranate arils", "sprouts", "walnuts", "macadamia nuts", "cashews", "pecans", "almonds", "pumpkin seeds", "pine nuts", "sunflower seeds", "bacon", "cheese", "feta cheese", "goat cheese", "croutons"]

def choose_ingredients(ingredients_list, count):
    selected = []
    for green in range(0, count):
        chosen_index = random.randint(0, len(ingredients_list) - 1)
        selected.append(ingredients_list[chosen_index])
        del ingredients_list[chosen_index]
    return selected

ingredients = []
ingredients += choose_ingredients(greens, random.randint(1, 3))
ingredients += choose_ingredients(vegetables, random.randint(2, 6))
ingredients += choose_ingredients(fruit, random.randint(1, 2))
ingredients += choose_ingredients(protein, random.randint(1, 2))
ingredients += choose_ingredients(grains, 1)
ingredients += choose_ingredients(dressing, 1)
ingredients += choose_ingredients(extras, random.randint(1, 3))

print(ingredients)

