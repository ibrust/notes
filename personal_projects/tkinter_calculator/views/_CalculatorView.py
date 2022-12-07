from tkinter import *
from tkinter import ttk
from ._BaseViewProtocol import BaseViewProtocol
from dataclasses import dataclass
from abc import ABC, abstractmethod


__all__ = ['CalculatorView', 'CalculatorViewDelegate']

class CalculatorViewDelegate(ABC):
    @abstractmethod
    def buttonTap(self):
        pass


class CalculatorView(BaseViewProtocol):
    @dataclass
    class Model:
        displayText: str = ""

    @property
    def viewModel(self):
        return self._viewModel

    @viewModel.setter
    def viewModel(self, viewModel: Model):
        if not isinstance(viewModel, CalculatorView.Model):
            raise TypeError("viewModel must be of type CalculatorView.Model")
        self._viewModel = viewModel
        self.applyModel()

    delegate: CalculatorViewDelegate

    def __init__(self):
        self.viewModel = CalculatorView.Model()
        self.delegate = None

    def constructViews(self):
        self.mainWindow: Tk = Tk()
        self.mainFrame: Frame = ttk.Frame(self.mainWindow, padding="3 3 12 12")

    def layoutViews(self):
        self.mainWindow.columnconfigure(0, weight=1)    # weight=1 allows the frame to resize as the window is resized
        self.mainWindow.rowconfigure(0, weight=1)

    def decorateViews(self):
        self.mainWindow.title("Calculator")

    def applyModel(self):
        pass