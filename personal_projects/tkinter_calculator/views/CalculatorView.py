from tkinter import *
from tkinter import ttk
from . import BaseView
from ..controllers import *


__all__ = ['CalculatorView']

class CalculatorView(BaseView):
    displayedString: str
    mainFrame: ttk.Frame

    def __init__(self):
        self.displayedString = "0"
        super.__init__()

    def constructViews(self):
        self.mainWindow = Tk()
        self.mainWindow.title("Calculator")
        self.mainFrame = ttk.Frame(self.mainWindow, padding="3 3 12 12")

    def layoutViews(self):
        self.mainWindow.columnconfigure(0, weight=1)    # weight=1 allows the frame to resize as the window is resized
        self.mainWindow.rowconfigure(0, weight=1)




