from ..views import *
from ..types import *
from ._MainControllerDelegate import MainControllerDelegate

__all__ = ['MainController']

class MainController(CalculatorViewDelegate):

    delegate: MainControllerDelegate

    def __init__(self):
        self.delegate = None
