from ..views import *
from ._MainControllerDelegate import MainControllerDelegate

__all__ = ['MainController']

class MainController(MainViewDelegate):

    delegate: MainControllerDelegate

    def __init__(self):
        self.delegate = None
