from reactivex.subject.subject import Subject
from ..controllers import CalculatorControllerDelegate
from ..types import ButtonSymbol, CalculatorMode

__all__ = ['CalculatorModel']

class CalculatorModel(CalculatorControllerDelegate):

    class Data:
        runningTotal: float
        hasPressedDecimal: bool

        def __init__(self):
            self.runningTotal = 0.0
            self.hasPressedDecimal = False
            self.mode = CalculatorMode.DECIMAL

    publisher: Subject

    def __init__(self):
        self.publisher = Subject()
        self.data = CalculatorModel.Data()

    @property
    def data(self):
        return self._data

    @data.setter
    def data(self, newData: Data):
        if not isinstance(newData, CalculatorModel.Data):
            raise TypeError("property must be of type CalculatorModel.Data")
        self._data = newData
        self.publisher.on_next(self._data)

    def add(self): pass

    def subtract(self): pass

    def multiply(self): pass

    def divide(self): pass

    def equals(self): pass

    def digitOrDecimalEntered(self, symbol: ButtonSymbol):
        if symbol != ButtonSymbol.DECIMAL:
            self.data.runningTotal = float(str(self.data.runningTotal) + symbol.value)
        else:
            self.data.hasPressedDecimal = True

    def changeMode(self):
        self.data.mode = CalculatorMode((self.data.mode.value + 1) % len(CalculatorMode))

    def clear(self):
        self.data.runningTotal = 0.0
        self.data.hasPressedDecimal = False
