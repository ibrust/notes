from enum import Enum

__all__ = ['CalculatorMode']
class CalculatorMode(Enum):
    BINARY = 0
    DECIMAL = 1
    HEX = 2