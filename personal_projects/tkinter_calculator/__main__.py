from .coordinators import CalculatorCoordinator
from tkinter import *

def main():
    mainWindow: Tk = Tk()
    mainWindow.columnconfigure(0, weight=1)
    mainWindow.rowconfigure(0, weight=1)
    mainWindow.title("Calculator")

    coordinator = CalculatorCoordinator(mainWindow)
    coordinator.start()

    mainWindow.mainloop()

    # would you setup some subscription here to fire a system event or close normally when the coordinator finished...?

main()