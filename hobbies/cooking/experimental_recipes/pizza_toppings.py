import random

basic_toppings = ["pepperoni", "mozzarella", "bacon", "garlic", "basil", "sausage", "onion", "white mushroom", "oregano", "sun-dried tomato", "parmigian", "cilantro",
                  "fresh basil", "black olives", "green olives", "bell pepper"]

advanced_toppings = ["buffalo mozzarella", "red pepper flakes", "ham", "chicken", "pineapple", "canadian bacon", "salami",
                     "jalepeno", "spinach", "asiago cheese", "provolone", "olive oil", "pesto", "feta cheese", "meatballs", "proscuito",
                     "beef", "caramelized onions", "barbeque sauce", "ricotta", "cherry tomatoes", "peperoncini", "banana peppers", "alfredo sauce",
                     "barbeque chicken, shredded", "arugula", "goat cheese", "artichoke hearts", "italian sweet pepper", "turkey bacon", "chorizo", "shrimp",
                     "scrambled egg", "fried egg", "capicola", "gorgonzola cheese", "emmental cheese", "anchovy", "gyro meet", "corn", "broccoli", "eggplant",
                     "fish", "crab", "oyster mushroom", "shitake mushroom", "capers", "smoked salmon", "beef", "refried beans", "salsa", "crushed tortilla chips",
                     "cheddar cheese", "blue cheese", "fig jam", "clams", "Pecorino Romano cheese", "roasted beets", "roasted potatoes", "pastrami", "swiss cheese",
                     "crème fraiche (similar to sour cream)", "green onion", "romaine lettuce", "homemade ranch dressing", "fresno or calabrian chili pepper",
                     "kale", "sauteed scallion greens", "chickpeas", "avocado", "wasabi", "navy beans", "snow peas", "green beans", "motery jack cheese", "fontiago cheese",
                     "jalapeno cheddar cheese", "squid", "breadcrumbs", "rice", "sesame seeds", "pistachios", "chili sauce", "focaccia bread (similar to pizza bread)",
                     "melon", "white chocolate", "honey", "bagels (instead of normal dough?)", "baguette (instead of normal dough?)", "thincrust", "cream", "grits", "scallops",
                     "mango-ginger seasoning", "mahlab (seasoning)", "white peppercorn", "poppy seed", "mace (seasoning)", "sumac (seasoning)", "pomegranate seed", "star anise",
                     "ginger", "black mustard", "Fenugreek", "Cardamom", "long pepper", "Clove", "jam", "sweet potato"]

full_toppings = basic_toppings + advanced_toppings

total_toppings = random.randint(8, 13);

final_list = []

for x in range(total_toppings):
    roll = random.randint(1,100)
    if (roll <= 39):
        choice = basic_toppings[random.randint(0, len(basic_toppings) - 1)]
    else:
        choice = full_toppings[random.randint(0, len(full_toppings) - 1)]
    if choice not in final_list:
        final_list.append(choice)


print(final_list)
