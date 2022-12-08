from enum import Enum, auto

__all__ = ['CalculatorMode']
class CalculatorMode(Enum):
    BINARY = auto()
    DECIMAL = auto()
    HEX = auto()