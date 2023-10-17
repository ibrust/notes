from .item_stats import ItemStats


# attackDamage: float
# attackSpeed: float
# criticalStrikeChance: float
# criticalStrikeDamage: float
# abilityHaste: float
# abilityPower: float
# armorPen: float
# magicPen: float
# lifesteal: float
# omnivamp: float
# lethality: float
# goldCost: int

class InfinityEdge(ItemStats):
    def __init__(self):
        super().__init__(65, 0, 20, 45, 0, 0, 0, 0, 0, 0, 0, 3400)

class BloodThirster(ItemStats):
    def __init__(self):
        super().__init__(55, 0, 20, 0, 0, 0, 0, 0, 18, 0, 0, 3400)

class RavenousHydra(ItemStats):
    def __init__(self):
        super().__init__(65, 0, 0, 0, 25, 0, 0, 0, 10, 0, 0, 3400)

class GaleForce(ItemStats):
    def __init__(self):
        super().__init__(50, 15, 20, 0, 0, 0, 0, 0, 0, 0, 0, 3400)

class NavoriQuickblades(ItemStats):
    def __init__(self):
        super().__init__(65, 0, 20, 0, 15, 0, 0, 0, 0, 0, 0, 3400)

class TrinityForce(ItemStats):
    def __init__(self):
        super().__init__(40, 33, 0, 0, 20, 0, 0, 0, 0, 0, 0, 3333)

class BladeOfTheRuinedKing(ItemStats):
    def __init__(self):
        super().__init__(40, 25, 0, 0, 0, 0, 0, 0, 8, 0, 0, 3300)

class SpearOfSojin(ItemStats):
    def __init__(self):
        super().__init__(65, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 3400)

class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3300)


class TitanicHydra(ItemStats):
    def __init__(self):
        super().__init__(30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3300)


class DeathsDance(ItemStats):
    def __init__(self):
        super().__init__(55, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 3300)


class StrideBreaker(ItemStats):
    def __init__(self):
        super().__init__(60, 20, 0, 0, 20, 0, 0, 0, 0, 0, 0, 3300)


class DivineSunderer(ItemStats):
    def __init__(self):
        super().__init__(40, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 3300)


class WitsEnd(ItemStats):
    def __init__(self):
        super().__init__(40, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3200)


class GoreDrinker(ItemStats):
    def __init__(self):
        super().__init__(55, 0, 0, 0, 20, 0, 0, 0, 0, 8, 0, 3200)


class SteryldasGrudge(ItemStats):
    def __init__(self):
        super().__init__(45, 0, 0, 0, 20, 0, 30, 0, 0, 0, 0, 3200)


class BlackCleaver(ItemStats):
    def __init__(self):
        super().__init__(50, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 3100)


class YomuusGhostblade(ItemStats):
    def __init__(self):
        super().__init__(60, 0, 0, 0, 15, 0, 0, 0, 0, 0, 18, 3100)


class DuskbladeOfDraktharr(ItemStats):
    def __init__(self):
        super().__init__(60, 0, 0, 0, 20, 0, 0, 0, 0, 0, 18, 3100)

class Eclipse(ItemStats):
    def __init__(self):
        super().__init__(60, 0, 0, 0, 15, 0, 0, 0, 0, 0, 12, 3100)


class MortalReminder(ItemStats):
    def __init__(self):
        super().__init__(40, 0, 20, 0, 0, 0, 30, 0, 0, 0, 0, 3000)


class LordDominiksRegards(ItemStats):
    def __init__(self):
        super().__init__(35, 0, 20, 0, 0, 0, 30, 0, 0, 0, 0, 3000)


class StatikkShiv(ItemStats):
    def __init__(self):
        super().__init__(45, 30, 20, 0, 0, 0, 0, 0, 0, 0, 0, 3000)

class RapidfireCannon(ItemStats):
    def __init__(self):
        super().__init__(30, 15, 20, 0, 0, 0, 0, 0, 0, 0, 0, 3000)

class StormRazor(ItemStats):
    def __init__(self):
        super().__init__(50, 15, 20, 0, 0, 0, 0, 0, 0, 0, 0, 3000)


class MercurialScimintar(ItemStats):
    def __init__(self):
        super().__init__(40, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 3000)


class HullBreaker(ItemStats):
    def __init__(self):
        super().__init__(60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3000)


class KrakenSlayer(ItemStats):
    def __init__(self):
        super().__init__(40, 30, 20, 0, 0, 0, 0, 0, 0, 0, 0, 3000)

class ImmortalShieldbow(ItemStats):
    def __init__(self):
        super().__init__(50, 0, 20, 0, 0, 0, 0, 0, 10, 0, 0, 3000)

class TheCollector(ItemStats):
    def __init__(self):
        super().__init__(55, 0, 20, 0, 0, 0, 0, 0, 0, 0, 18, 3000)


class ProwlersClaw(ItemStats):
    def __init__(self):
        super().__init__(55, 0, 0, 0, 15, 0, 0, 0, 0, 0, 18, 3000)


class AxiomArc(ItemStats):
    def __init__(self):
        super().__init__(55, 0, 0, 0, 25, 0, 0, 0, 0, 0, 18, 3000)


class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)

class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)


class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)


class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)

class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)


class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)


class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)

class X(ItemStats):
    def __init__(self):
        super().__init__(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3400)