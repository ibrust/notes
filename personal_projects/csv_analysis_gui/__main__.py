from .coordinators import MainCoordinator, MainCoordinatorResult
from tkinter import *
import sys

def main():
    mainWindow: Tk = Tk()
    mainWindow.title("GIS Data Analyzer")
    mainWindow.resizable(False, False)

    coordinator = MainCoordinator(mainWindow)

    def callback(result: MainCoordinatorResult):
        if result != MainCoordinatorResult.SUCCESS:
            print(result, result.value)
            sys.exit(f"Error - Coordinator Exited with {result}")

    coordinator.onFinishCallback = callback
    coordinator.start()

    mainWindow.mainloop()                       # blocks until the user closes the window

    coordinator.finish(MainCoordinatorResult.SUCCESS)

main()