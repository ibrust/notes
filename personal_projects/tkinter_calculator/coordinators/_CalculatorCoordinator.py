from ..models import CalculatorModel
from ..views import CalculatorView
from ..presenters import CalculatorPresenter
from ..controllers import CalculatorController


__all__ = ['CalculatorCoordinator']

class CalculatorCoordinator:

    def __init__(self):
        self.model = CalculatorModel()
        self.presenter = CalculatorPresenter()
        self.view = CalculatorView()
        self.controller = CalculatorController()

    def start(self):
        self.setupPublishers()
        self.view.delegate = self.controller
        self.controller.delegate = self.model

    def setupPublishers(self):
        def updatePresenter(model: CalculatorModel.Data):
            self.presenter.model = model

        def updateView(viewModel: CalculatorView.Model):
            self.view.viewModel = viewModel

        self.model.publisher.subscribe(
            on_next=updatePresenter
        )
        self.presenter.publisher.subscribe(
            on_next=updateView
        )

    def finish(self):
        pass