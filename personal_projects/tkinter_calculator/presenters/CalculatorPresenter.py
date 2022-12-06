from ..models import CalculatorModel

__all__ = ['CalculatorPresenter']

class CalculatorPresenter:

    def __init__(self, model: CalculatorModel):
        self.model = model