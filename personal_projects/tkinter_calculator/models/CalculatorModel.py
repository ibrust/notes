from reactivex.subject.subject import Subject
from dataclasses import dataclass
from controllers import CalculatorControllerDelegate

__all__ = ['CalculatorModel']

class CalculatorModel(CalculatorControllerDelegate):
    @dataclass
    class Data:
        mathExpression: str

    publisher: Subject

    def __init__(self):
        self.publisher = Subject()
        self.data = CalculatorModel.Data()

    @property
    def data(self):
        return self._data

    @data.setter
    def data(self, value):
        if not isinstance(value, CalculatorModel.Data):
            raise TypeError("property must be of type CalculatorModel.Data")
        self._data = value
        self.publisher.on_next(self._data)
