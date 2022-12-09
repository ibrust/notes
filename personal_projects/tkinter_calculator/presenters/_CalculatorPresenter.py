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
        self.modelData = None

    @property
    def modelData(self):
        return None

    @modelData.setter
    def modelData(self, modelData: CalculatorModel.Data):
        if modelData is None:
            return
        if not isinstance(modelData, CalculatorModel.Data):
            raise TypeError("model must be of type CalculatorModel.Data")

        self.publisher.on_next(self._createViewModel(modelData))

    def _createViewModel(self, modelData: CalculatorModel.Data) -> CalculatorView.Model:
        return CalculatorView.Model(modelData.currentOperand)
