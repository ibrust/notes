from tkinter import *
from tkinter import ttk
from ._BaseViewProtocol import BaseViewProtocol
from dataclasses import dataclass
from abc import ABC, abstractmethod
from ._DecimalButtonsView import DecimalButtonsView
from ._OperationButtonsView import OperationButtonsView
from ._UtilityButtonsView import UtilityButtonsView
from enum import Enum, auto


__all__ = ['CalculatorView', 'CalculatorViewDelegate']

class CalculatorViewDelegate(ABC):
    @abstractmethod
    def buttonTap(self):
        pass

class CalculatorMode(Enum):
    BINARY = auto()
    DECIMAL = auto()
    HEX = auto()

class CalculatorView(BaseViewProtocol):
    @dataclass
    class Model:
        displayText: str = ""
        mode: CalculatorMode = CalculatorMode.DECIMAL

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

    def __init__(self, superView: Frame):
        self.viewModel = CalculatorView.Model()
        self.delegate = None
        self.superView = superView

    def constructViews(self):
        self.mainFrame: Frame = ttk.Frame(self.superView)
        self.mathExpressionLabel = ttk.Label(self.mainFrame, text="0")

        self.numericalButtonsViewFrame: Frame = ttk.Frame(self.mainFrame)
        self.numericalButtonsView = DecimalButtonsView(self.numericalButtonsViewFrame)

        self.operationalButtonsViewFrame: Frame = ttk.Frame(self.mainFrame)
        self.operationalButtonsView = OperationButtonsView(self.operationalButtonsViewFrame)

        self.utilityButtonsViewFrame: Frame = ttk.Frame(self.mainFrame)
        self.utilityButtonsView = UtilityButtonsView(self.utilityButtonsViewFrame)

    def layoutViews(self):
        self.mainFrame.grid(column=0, row=0, sticky=(N, W, E, S))
        self.mainFrame.columnconfigure(0, weight=1)
        self.mainFrame.rowconfigure(0, weight=1)
        self.mainFrame.configure(padding=8)

        self.mathExpressionLabel.grid(column=0, columnspan=2, row=0, sticky=(N, W, E))
        self.mathExpressionLabel.configure(padding="5")

        self.numericalButtonsViewFrame.grid(column=0, row=2, sticky=(N, S, W, E))
        self.numericalButtonsViewFrame.columnconfigure(0, weight=1)
        self.numericalButtonsViewFrame.rowconfigure(0, weight=1)

        self.operationalButtonsViewFrame.grid(column=1, row=1, rowspan=2, sticky=(N, S, W, E))
        self.operationalButtonsViewFrame.columnconfigure(0, weight=1)
        self.operationalButtonsViewFrame.rowconfigure(0, weight=1)

        self.utilityButtonsViewFrame.grid(column=0, row=1, sticky=(N, S, W, E))
        self.utilityButtonsViewFrame.columnconfigure(0, weight=1)
        self.utilityButtonsViewFrame.rowconfigure(0, weight=1)

    def styleViews(self):
        frameStyle = ttk.Style(self.superView)
        frameStyle.theme_use("alt")
        frameStyle.configure("CalculatorViewMainFrame.TFrame", background="green", borderwidth=5, relief='raised')
        self.mainFrame.configure(style="CalculatorViewMainFrame.TFrame")

        mathExpressionStyle = ttk.Style(self.mainFrame)
        mathExpressionStyle.theme_use("alt")
        mathExpressionStyle.configure("CalculatorViewMathExpression.TLabel", foreground="white", background="black", borderwidth=1, relief='raised')
        self.mathExpressionLabel.configure(style="CalculatorViewMathExpression.TLabel")

        numericalButtonsViewFrameStyle = ttk.Style(self.mainFrame)
        numericalButtonsViewFrameStyle.theme_use("alt")
        numericalButtonsViewFrameStyle.configure("numericalButtonsViewFrame.TFrame", background="orange", borderwidth=1, relief='raised')
        self.numericalButtonsViewFrame.configure(style="numericalButtonsViewFrame.TFrame")

        self.utilityButtonsViewFrame.configure(style="numericalButtonsViewFrame.TFrame")

    def applyModel(self):
        pass