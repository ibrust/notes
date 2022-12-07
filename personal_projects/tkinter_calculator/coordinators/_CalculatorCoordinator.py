from ..models import CalculatorModel
from ..views import CalculatorView
from ..presenters import CalculatorPresenter
from ..controllers import CalculatorController
from tkinter import *


__all__ = ['CalculatorCoordinator']

class CalculatorCoordinator:

    def __init__(self, superview: Tk | Frame):
        self.model = CalculatorModel()
        self.presenter = CalculatorPresenter()
        self.view = CalculatorView(superview)
        self.controller = CalculatorController()

    def start(self):
        self.setupPublishers()

        self.view.delegate = self.controller
        self.controller.delegate = self.model

    def setupPublishers(self):
        def updatePresenter(modelData: CalculatorModel.Data):
            self.presenter.modelData = modelData

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