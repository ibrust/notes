from .coordinators import MainCoordinator, MainCoordinatorResult
from tkinter import *
import sys
import tkintermapview

def main():
    mainWindow: Tk = Tk()
    mainWindow.title("GIS Dataset Analyzer")
    mainWindow.resizable(False, False)
    mainWindow.geometry(f"{800}x{600}")

    # coordinator = MainCoordinator(mainWindow)

    map_widget = tkintermapview.TkinterMapView(mainWindow, width=800, height=600, corner_radius=0)
    map_widget.place(relx=0.5, rely=0.5, anchor=CENTER)

    def callback(result: MainCoordinatorResult):
        if result != MainCoordinatorResult.SUCCESS:
            print(result, result.value)
            sys.exit(f"Error - Coordinator Exited with {result}")

    # coordinator.onFinishCallback = callback
    # coordinator.start()

    mainWindow.mainloop()                       # blocks until the user closes the window

    # coordinator.finish(MainCoordinatorResult.SUCCESS)

main()