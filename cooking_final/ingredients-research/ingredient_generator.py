import sys
import random

nuts = [
    "almonds",
    "cashews",
    "peanuts",
    "pecans",
    "walnuts",
    "pistachios",
    "brazil nuts",
    "hazelnuts",
    "macademia nuts",
    "pine nuts",
    "chestnuts",
]

spices = [
    "Allspice", 
    "Anise Seed", 
    "Black Pepper", 
    "Caraway Seeds", 
    "Cardamom", 
    "Cayenne Pepper", 
    "Celery Seed", 
    "Cinnamon", 
    "Cloves", 
    "Coriander Seed", 
    "Cumin", 
    "Dill Seed", 
    "Fennel Seeds", 
    "Fenugreek", 
    "Garlic Powder", 
    "Ginger", 
    "Mace", 
    "Mustard Powder", 
    "Nutmeg", 
    "Onion Powder", 
    "Paprika", 
    "Poppy Seeds", 
    "Red Pepper Flakes", 
    "Saffron", 
    "Sesame Seeds", 
    "Smoked Paprika", 
    "Star Anise", 
    "Sumac", 
    "Sichuan Peppercorns", 
    "Turmeric", 
    "Vanilla Bean", 
    "White Pepper"
]

herbs = [
    "Basil", 
    "Bay Leaves", 
    "Chervil", 
    "Chives", 
    "Cilantro", 
    "Dill Weed", 
    "Epazote", 
    "Fenugreek Leaves", 
    "Kaffir Lime Leaves", 
    "Lavender", 
    "Lemon Balm", 
    "Lemongrass", 
    "Lemon Verbena", 
    "Lovage", 
    "Marjoram", 
    "Mint", 
    "Oregano", 
    "Parsley", 
    "Rosemary", 
    "Sage", 
    "Savory", 
    "Sorrel", 
    "Tarragon", 
    "Thyme"
]

def main():
    options = {
        "nuts": nuts,
        "spices": spices,
        "herbs": herbs
    }

    if len(sys.argv) < 2:
        print("Usage: python script.py [nuts|spices|herbs]")
        sys.exit(1)

    selection = sys.argv[1].lower()

    if selection in options:
        chosen_item = random.choice(options[selection])
        print(chosen_item)
    else if selection == "random":
        chosen_item = random.choice(nuts + herbs + spices)
        print(chosen_item)
    else:
        print(f"Error: '{selection}' is not a valid category.")
        print("Available categories: nuts, spices, herbs")

if __name__ == "__main__":
    main()