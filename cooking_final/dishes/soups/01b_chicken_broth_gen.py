import random

cooking_method = ["simmer", "crock-pot", "pressure-cooker"][random.randint(0, 2)]
print("cooking method: ", cooking_method)

broth_type = ["regular broth", "stock", "bone broth"][random.randint(0, 2)]
print("broth type: ", broth_type)

cultural_variation_inspiration = ["Mirepoix", "Soffritto", "Suppengrün", "Holy Trinity", "Zazharka"][random.randint(0, 4)]
print("cultural variation (or inspiration / techniques to use, perhaps): ", cultural_variation_inspiration)

extra_aromatic = ["Leek", "Parsnip", "Fennel", "Mushroom", "Garlic", "Tomato"][random.randint(0, 5)]
print("extra aromatic: ", extra_aromatic)

extra_herb = ["Parsley", "Thyme", "Rosemary (use sparingly)", "Bay Leaf", "Sage", "Oregano", "Marjoram", "Tarragon", "Dill"][random.randint(0, 8)]
print("extra herb: ", extra_herb)

extra_spice = ["Peppercorns", "Coriander", "Star Anise", "Cloves", "Cinnamon", "Ginger", "Turmeric", "Smoked Paprika"][random.randint(0, 7)]
print("extra herb: ", extra_spice)

extra_umami = ["Roasted Bones", "Dried Mushrooms", "Kombu", "Miso Paste", "Tomato Paste", "Parmesan Rind", "Soy Sauce / Fish Sauce"][random.randint(0, 6)]
print("extra umami: ", extra_umami)

extra_acid = ["Lemon Juice/Zest", "Vinegar (Apple Cider, White Wine, Rice)", "Wine (White or Dry Sherry)", "Tomato (Paste, Fresh, or Canned)"][random.randint(0, 3)]
print("extra acid: ", extra_acid)

chicken_parts = ["Whole Chicken", "Bones-Only", "Carcass"][random.randint(0, 2)]
print("parts to use: ", chicken_parts)
