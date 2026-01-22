from reactivex.subject.subject import Subject
from ..models import MainModel
from ..views import MainView

__all__ = ['MainPresenter']

class MainPresenter:

    publisher: Subject
    def __init__(self):
        self.publisher = Subject()
        self.modelData = None

    @property
    def modelData(self):
        return None

    @modelData.setter
    def modelData(self, modelData: MainModel.Data):
        if modelData is None:
            return
        if not isinstance(modelData, MainModel.Data):
            raise TypeError("model must be of type MainModel.Data")

        self.publisher.on_next(self._createViewModel(modelData))

    def _createViewModel(self, modelData: MainModel.Data) -> MainView.Model:
        return MainView.Model()
