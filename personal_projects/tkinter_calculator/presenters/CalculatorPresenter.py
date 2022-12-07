from reactivex.subject.subject import Subject
from ..models import CalculatorModel
from ..views import CalculatorView

__all__ = ['CalculatorPresenter']

# features to support:
# shift button for A-F
# disabling buttons other than 0/1 in binary mode or 0-7 in octal mode
# buttons for switching between binary / octal / hex / decimal

class CalculatorPresenter:

    publisher: Subject
    def __init__(self):
        self.publisher = Subject()
        self.model = None

    @property
    def model(self):
        return None

    @model.setter
    def model(self, model: CalculatorModel.Data):
        if not isinstance(model, CalculatorModel.Data):
            raise TypeError("model must be of type CalculatorModel.Data")

        self.publisher.on_next(self._createViewModel(model))

    def _createViewModel(self, model: CalculatorModel.Data) -> CalculatorView.Model:
        return CalculatorView.Model(model.mathExpression)
