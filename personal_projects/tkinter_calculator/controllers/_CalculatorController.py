from ..views import CalculatorViewDelegate
from abc import ABC, abstractmethod

__all__ = ['CalculatorController', 'CalculatorControllerDelegate']

class CalculatorControllerDelegate(ABC):
    @abstractmethod
    def add(self, operand1, operand2): pass
    @abstractmethod
    def subtract(self, operand1, operand2): pass

class CalculatorController(CalculatorViewDelegate):

    delegate: CalculatorControllerDelegate

    def __init__(self):
        self.delegate = None

    def buttonTap(self):
        pass
