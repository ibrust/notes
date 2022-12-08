from ..views import CalculatorViewDelegate
from abc import ABC, abstractmethod
from ..helper import ButtonSymbol

__all__ = ['CalculatorController', 'CalculatorControllerDelegate']

class CalculatorControllerDelegate(ABC):
    @abstractmethod
    def add(self, newOperand: ButtonSymbol): pass

    @abstractmethod
    def subtract(self, newOperand: ButtonSymbol): pass

    @abstractmethod
    def multiply(self, newOperand: ButtonSymbol): pass

    @abstractmethod
    def divide(self, newOperand: ButtonSymbol): pass

    @abstractmethod
    def equals(self, newOperand: ButtonSymbol): pass

class CalculatorController(CalculatorViewDelegate):

    delegate: CalculatorControllerDelegate

    def __init__(self):
        self.delegate = None

    def buttonTap(self, symbol: ButtonSymbol):
        print(symbol)
