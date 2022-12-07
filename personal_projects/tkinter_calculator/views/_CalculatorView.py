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

    def __init__(self, superView: Tk | Frame):
        self.viewModel = CalculatorView.Model()
        self.delegate = None
        self.superView = superView

    def constructViews(self):
        self.mainFrame: Frame = ttk.Frame(self.superView, padding="3 3 12 12")

    def layoutViews(self):
        pass

    def decorateViews(self):
        pass

    def applyModel(self):
        pass