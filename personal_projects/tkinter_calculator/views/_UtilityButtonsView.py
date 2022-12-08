from tkinter import *
from tkinter import ttk
from ._BaseViewProtocol import BaseViewProtocol
from abc import ABC, abstractmethod
from ..types import ButtonSymbol


class UtilityButtonsViewDelegate(ABC):
    @abstractmethod
    def buttonTap(self, symbol: ButtonSymbol):
        pass

class UtilityButtonsView(BaseViewProtocol):

    delegate: UtilityButtonsViewDelegate

    def __init__(self, superView: Frame):
        self.delegate = None
        self.superView = superView

    def constructViews(self):
        self.mainFrame: Frame = ttk.Frame(self.superView, padding="0")

        symbols = [ButtonSymbol.MODE, ButtonSymbol.CLEAR]
        self.buttons: [Button] = []
        for i in range(0, 2):
            button: Button = ttk.Button(self.mainFrame, text=symbols[i].value, command=lambda i=i: self.buttonTap(symbols[i]))
            self.buttons.append(button)

    def layoutViews(self):
        self.mainFrame.grid(column=0, row=0, sticky=(N, W, E, S))

        self.buttons[0].grid(column=0, columnspan=2, row=0, padx=0, pady=0, sticky=(N, E, S, W))
        self.buttons[0].configure(width=11)
        self.buttons[1].grid(column=2, row=0, padx=0, pady=0, sticky=(N, E, S, W))
        self.buttons[1].configure(width=5)

    def styleViews(self):
        frameStyle = ttk.Style(self.superView)
        frameStyle.theme_use("alt")
        frameStyle.configure("UtilityButtonsViewMainFrame.TFrame", background="brown", borderwidth=1, relief='raised')
        self.mainFrame.configure(style="UtilityButtonsViewMainFrame.TFrame")

        buttonStyle = ttk.Style(self.superView)
        buttonStyle.configure("UtilityButton.TButton", foreground="black", background="brown", borderwidth=1, relief='raised')
        for button in self.buttons:
            button.configure(style="UtilityButton.TButton")

    def buttonTap(self, symbol: ButtonSymbol):
        self.delegate.buttonTap(symbol)