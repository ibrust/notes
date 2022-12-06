from ..views import CalculatorView
from ..models import CalculatorModel

__all__ = ['CalculatorController']

class CalculatorController:
    view: CalculatorView
    model: CalculatorModel

    def __init__(self, model: CalculatorModel):
        self.view = CalculatorView()
        self.app = model