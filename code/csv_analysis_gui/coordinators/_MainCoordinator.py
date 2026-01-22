from ..models import MainModel
from ..views import MainView
from ..presenters import MainPresenter
from ..controllers import MainController
from ._BaseCoordinatorProtocol import *
from tkinter import *
from enum import auto


__all__ = ['MainCoordinator', 'MainCoordinatorResult']


class MainCoordinatorResult(CoordinatorResult):
    SUCCESS = auto()
    FAILURE = auto()


class MainCoordinator(BaseCoordinatorProtocol):

    def __init__(self, superview: Frame):
        self.model = MainModel()
        self.presenter = MainPresenter()
        self.view = MainView(superview)
        self.controller = MainController()

    def start(self):
        self.setupPublishers()

        self.view.delegate = self.controller
        self.controller.delegate = self.model

    def setupPublishers(self):
        def updatePresenter(modelData: MainModel.Data):
            self.presenter.modelData = modelData

        def updateView(viewModel: MainView.Model):
            self.view.viewModel = viewModel

        self.model.publisher.subscribe(
            on_next=updatePresenter
        )
        self.presenter.publisher.subscribe(
            on_next=updateView
        )

    def finish(self, result: CoordinatorResult):
        pass