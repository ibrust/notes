from .coordinators import CalculatorCoordinator, CalculatorCoordinatorResult
from tkinter import *
import sys

def main():
    mainWindow: Tk = Tk()
    mainWindow.columnconfigure(0, weight=1)
    mainWindow.rowconfigure(0, weight=1)
    mainWindow.title("Calculator")

    coordinator = CalculatorCoordinator(mainWindow)

    def callback(result: CalculatorCoordinatorResult):
        if result != CalculatorCoordinatorResult.SUCCESS:
            print(result, result.value)
            sys.exit(f"Error - Coordinator Exited with {result}")

    coordinator.onFinishCallback = callback
    coordinator.start()

    mainWindow.mainloop()                       # blocks until the user closes the window

    coordinator.finish(CalculatorCoordinatorResult.SUCCESS)

main()