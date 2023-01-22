from abc import ABC, abstractmethod
from .._types import ButtonSymbol

__all__ = ['CalculatorControllerDelegate']

class CalculatorControllerDelegate(ABC):
    @abstractmethod
    def equals(self): pass

    @abstractmethod
    def digitOrDecimalEntered(self, symbol: ButtonSymbol): pass

    @abstractmethod
    def handleMathOperation(self, symbol: ButtonSymbol): pass

    @property
    @abstractmethod
    def mode(self): pass

    @mode.setter
    @abstractmethod
    def mode(self, value): pass

    @abstractmethod
    def clear(self): pass

    @abstractmethod
    def toggleSign(self): pass