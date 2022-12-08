from tkinter import *
from tkinter import ttk
from ._BaseViewProtocol import BaseViewProtocol
from abc import ABC, abstractmethod
from enum import Enum, auto


class UtilityButtonsViewDelegate(ABC):
    @abstractmethod
    def buttonTap(self):
        pass

class UtilityButtonsView(BaseViewProtocol):

    delegate: UtilityButtonsViewDelegate

    def __init__(self, superView: Frame):
        self.delegate = None
        self.superView = superView

    def constructViews(self):
        self.mainFrame: Frame = ttk.Frame(self.superView, padding="0")

        symbols = ["M", "^", "C"]
        self.buttons: [Button] = []
        for i in range(0, 3):
            button: Button = ttk.Button(self.mainFrame, text=symbols[i])
            self.buttons.append(button)

    def layoutViews(self):
        self.mainFrame.grid(column=0, row=0, sticky=(N, W, E, S))

        for i in range(0, 3):
            self.buttons[i].configure(width=5)
            self.buttons[i].grid(column=i, row=0, padx=0, pady=0, sticky=(N, E, S, W))

    def styleViews(self):
        frameStyle = ttk.Style(self.superView)
        frameStyle.theme_use("alt")
        frameStyle.configure("UtilityButtonsViewMainFrame.TFrame", background="brown", borderwidth=1, relief='raised')
        self.mainFrame.configure(style="UtilityButtonsViewMainFrame.TFrame")

        buttonStyle = ttk.Style(self.superView)
        buttonStyle.configure("UtilityButton.TButton", foreground="black", background="brown", borderwidth=1, relief='raised')
        for button in self.buttons:
            button.configure(style="UtilityButton.TButton")
