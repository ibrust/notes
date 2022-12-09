from reactivex.subject.subject import Subject
from ..controllers import CalculatorControllerDelegate
from ..types import ButtonSymbol, CalculatorMode
from ..helpers import reactiveProperty

__all__ = ['CalculatorModel']

class CalculatorModel(CalculatorControllerDelegate):

    class Data:
        previousOperand: str = reactiveProperty("previousOperand", str)
        currentOperand: str = reactiveProperty("currentOperand", str)
        mode: CalculatorMode.DECIMAL = reactiveProperty("mode", CalculatorMode)
        publisher: Subject
        def __init__(self):
            self.publisher = Subject()
            self.previousOperand = None
            self.currentOperand = "0"
            self.mode = CalculatorMode.DECIMAL

    publisher: Subject
    data: Data

    def __init__(self):
        self.publisher = Subject()
        self.data = CalculatorModel.Data()
        self.nextOperation = None
        self.didJustPressMathOperationButton = False

        self.data.publisher.subscribe(
            on_next=lambda v: self.publisher.on_next(self.data)
        )

    def handleMathOperation(self, nextOperation: ButtonSymbol):
        self.didJustPressMathOperationButton = True
        self.equals()
        self.nextOperation = nextOperation

    def equals(self):
        if self.data.previousOperand is not None and self.nextOperation is not None:
            self.data.currentOperand = str(float(self.data.currentOperand) + float(self.data.previousOperand))
            self.nextOperation = None
            self.data.previousOperand = None

    def digitOrDecimalEntered(self, symbol: ButtonSymbol):
        if self.didJustPressMathOperationButton:
            self.data.previousOperand = self.data.currentOperand
            self.data.currentOperand = symbol.value
            self.didJustPressMathOperationButton = False
            return
        if self.data.currentOperand == "0":
            self.data.currentOperand = symbol.value
            return
        if symbol != ButtonSymbol.DECIMAL or not "." in self.data.currentOperand:
            self.data.currentOperand = self.data.currentOperand + symbol.value
            return

    def changeMode(self):
        self.data.mode = CalculatorMode((self.data.mode.value + 1) % len(CalculatorMode))

    def clear(self):
        self.data.currentOperand = "0"
        self.data.previousOperand = None
        self.data.hasPressedDecimal = False
