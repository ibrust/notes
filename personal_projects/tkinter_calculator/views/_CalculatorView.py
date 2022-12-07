from tkinter import *
from tkinter import ttk
from ._BaseViewProtocol import BaseViewProtocol
from dataclasses import dataclass
from abc import ABC, abstractmethod
from ._DigitsView import DigitsView


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
        self.mainFrame: Frame = ttk.Frame(self.superView, padding="0")
        self.mathExpressionLabel = ttk.Label(self.mainFrame, text="0")
        self.digitsViewFrame: Frame = ttk.Frame(self.mainFrame, padding="0")
        self.digitsView = DigitsView(self.digitsViewFrame)

    def layoutViews(self):
        self.mainFrame.grid(column=0, row=0, sticky=(N, W, E, S))

        self.mathExpressionLabel.grid(column=0, row=1, padx=15, pady=15)
        self.digitsViewFrame.grid(column=0, row=2, padx=15, pady=15)

    def styleViews(self):
        frameStyle = ttk.Style(self.superView)
        frameStyle.theme_use("alt")
        frameStyle.configure("Frame1.TFrame", background="green", borderwidth=5, relief='raised')
        self.mainFrame.configure(style="Frame1.TFrame")

        mathExpressionStyle = ttk.Style(self.mainFrame)
        mathExpressionStyle.theme_use("alt")
        mathExpressionStyle.configure("Label1.TLabel", foreground="white", background="black", borderwidth=1, relief='raised')
        self.mathExpressionLabel.configure(style="Label1.TLabel")

        digitsViewFrameStyle = ttk.Style(self.mainFrame)
        digitsViewFrameStyle.theme_use("alt")
        digitsViewFrameStyle.configure("Frame2.TFrame", background="orange", borderwidth=1, relief='raised')
        self.digitsViewFrame.configure(style="Frame2.TFrame")

    def applyModel(self):
        pass