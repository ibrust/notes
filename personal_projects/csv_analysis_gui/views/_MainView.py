from tkinter import *
from tkinter import ttk
from ._BaseViewProtocol import BaseViewProtocol
from ._MainViewDelegate import MainViewDelegate

__all__ = ['MainView']

class MainView(BaseViewProtocol):

    class Model:
        def __init__(self):
            pass

    @property
    def viewModel(self):
        return self._viewModel

    @viewModel.setter
    def viewModel(self, viewModel: Model):
        if not isinstance(viewModel, MainView.Model):
            raise TypeError("viewModel must be of type MainView.Model")
        self._viewModel = viewModel
        self.applyModel()

    delegate: MainViewDelegate

    def __init__(self, superView: Frame):
        self.viewModel = MainView.Model()
        self.delegate = None
        self.superView = superView

    def constructViews(self):
        self.mainFrame: Frame = ttk.Frame(self.superView)

    def layoutViews(self):
        self.mainFrame.grid(column=0, row=0, sticky=(N, W, E, S))
        self.mainFrame.columnconfigure(0, weight=1)
        self.mainFrame.rowconfigure(0, weight=1)
        self.mainFrame.configure(padding=8)

    def styleViews(self):
        frameStyle = ttk.Style(self.superView)
        frameStyle.theme_use("alt")
        frameStyle.configure("MainViewMainFrame.TFrame", background="black", borderwidth=5, relief='raised')
        self.mainFrame.configure(style="MainViewMainFrame.TFrame")

    def applyModel(self):
        if not hasattr(self, "runningTotalLabel"):
            return