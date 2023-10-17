

class ItemStats:
    attackDamage: float
    attackSpeed: float
    criticalStrikeChance: float
    criticalStrikeDamage: float
    abilityHaste: float
    abilityPower: float
    armorPen: float
    magicPen: float
    lifesteal: float
    omnivamp: float
    lethality: float
    goldCost: int

    def __init__(self,
                 attackDamage: float = 0,
                 attackSpeed: float = 0,
                 criticalStrikeChance: float = 0,
                 criticalStrikeDamage: float = 0,
                 abilityHaste: float = 0,
                 abilityPower: float = 0,
                 armorPen: float = 0,
                 magicPen: float = 0,
                 lifesteal: float = 0,
                 omnivamp: float = 0,
                 lethality: float = 0,
                 goldCost: int = 0):
        self.attackDamage = attackDamage
        self.attackSpeed = attackSpeed
        self.criticalStrikeChance = criticalStrikeChance
        self.criticalStrikeDamage = criticalStrikeDamage
        self.abilityHaste = abilityHaste
        self.abilityPower = abilityPower
        self.armorPen = armorPen
        self.magicPen = magicPen
        self.lifesteal = lifesteal
        self.omnivamp = omnivamp
        self.lethality = lethality
        self.goldCost = goldCost


