from ..views import CalculatorViewDelegate
from abc import ABC, abstractmethod
from ..types import ButtonSymbol
from ..types import CalculatorMode

__all__ = ['CalculatorController', 'CalculatorControllerDelegate']

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

class CalculatorController(CalculatorViewDelegate):

    delegate: CalculatorControllerDelegate

    def __init__(self):
        self.delegate = None

    def buttonTap(self, symbol: ButtonSymbol):
        if symbol.isDigitOrDecimal():
            self.delegate.digitOrDecimalEntered(symbol)
        elif symbol.isMathOperation():
            self.delegate.handleMathOperation(symbol)
        elif symbol == ButtonSymbol.EQUALS:
            self.delegate.equals()
        elif symbol == ButtonSymbol.MODE:
            self.delegate.mode = CalculatorMode((self.delegate.mode.value + 1) % len(CalculatorMode))
        elif symbol == ButtonSymbol.CLEAR:
            self.delegate.clear()
