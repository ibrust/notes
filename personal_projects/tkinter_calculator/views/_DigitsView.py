from tkinter import *
from tkinter import ttk
from ._BaseViewProtocol import BaseViewProtocol
from dataclasses import dataclass
from abc import ABC, abstractmethod
from enum import Enum, auto
import math


class DigitsViewDelegate(ABC):
    @abstractmethod
    def buttonTap(self):
        pass


class CalculatorMode(Enum):
    BINARY = auto()
    DECIMAL = auto()
    HEX = auto()

class DigitsView(BaseViewProtocol):

    @dataclass
    class Model:
        mode: CalculatorMode = CalculatorMode.DECIMAL

    @property
    def viewModel(self):
        return self._viewModel

    @viewModel.setter
    def viewModel(self, viewModel: Model):
        if not isinstance(viewModel, DigitsView.Model):
            raise TypeError("viewModel must be of type DigitsView.Model")
        self._viewModel = viewModel
        self.applyModel()

    delegate: DigitsViewDelegate

    def __init__(self, superView: Tk | Frame):
        self.viewModel = DigitsView.Model()
        self.delegate = None
        self.superView = superView

    def constructViews(self):
        self.mainFrame: Frame = ttk.Frame(self.superView, padding="0")

        self.digitButtons: [Button] = []
        for i in range(1, 10):
            self.digitButtons.append(ttk.Button(self.mainFrame, text=str(i)))
        self.digitButtons.append(ttk.Button(self.mainFrame, text="0"))


    def layoutViews(self):
        self.mainFrame.grid(column=0, row=0, sticky=(N, W, E, S))

        for i in range(1, 10):
            col = ((i - 1) % 3) + 1
            row = math.ceil(i / 3)
            self.digitButtons[i - 1].configure(width=2)
            self.digitButtons[i-1].grid(column=col, row=row, padx=1, pady=1)
        self.digitButtons[-1].configure(width=2)
        self.digitButtons[-1].grid(column=1, row=4, padx=1, pady=1)

    def styleViews(self):
        frameStyle = ttk.Style(self.superView)
        frameStyle.theme_use("alt")
        frameStyle.configure("Frame3.TFrame", background="yellow", borderwidth=1, relief='raised')
        self.mainFrame.configure(style="Frame3.TFrame")

        buttonStyle = ttk.Style(self.superView)
        buttonStyle.configure("Button1.TButton", foreground="black", background="green", borderwidth=1, relief='raised')
        for button in self.digitButtons:
            button.configure(style="Button1.TButton")

    def applyModel(self):
        pass