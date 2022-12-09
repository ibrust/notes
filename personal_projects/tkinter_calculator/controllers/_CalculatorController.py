from ..views import CalculatorViewDelegate
from abc import ABC, abstractmethod
from ..types import ButtonSymbol

__all__ = ['CalculatorController', 'CalculatorControllerDelegate']

class CalculatorControllerDelegate(ABC):
    @abstractmethod
    def equals(self): pass
    @abstractmethod
    def digitOrDecimalEntered(self, symbol: ButtonSymbol): pass
    @abstractmethod
    def handleMathOperation(self, symbol: ButtonSymbol): pass
    @abstractmethod
    def changeMode(self): pass
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
            print("here")
            self.delegate.handleMathOperation(symbol)
        elif symbol == ButtonSymbol.EQUALS:
            self.delegate.equals()
        elif symbol == ButtonSymbol.MODE:
            self.delegate.changeMode()
        elif symbol == ButtonSymbol.CLEAR:
            self.delegate.clear()
