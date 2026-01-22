from ._MainControllerDelegate import MainControllerDelegate
from ..views import *

__all__ = ['MainController']

class MainController(MainViewDelegate):

    delegate: MainControllerDelegate

    def __init__(self):
        self.delegate = None
