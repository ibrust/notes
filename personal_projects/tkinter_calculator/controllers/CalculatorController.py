from ..views import CalculatorViewDelegate
from abc import ABC, abstractmethod

__all__ = ['CalculatorController', 'CalculatorControllerDelegate']


class CalculatorController(CalculatorViewDelegate):

    def __init__(self):
        pass

class CalculatorControllerDelegate(ABC):
    @abstractmethod
    def add(self, operand1, operand2): pass
    @abstractmethod
    def subtract(self, operand1, operand2): pass