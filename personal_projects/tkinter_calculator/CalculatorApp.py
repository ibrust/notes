from models import CalculatorModel
from views import CalculatorView
from presenters import CalculatorPresenter
from controllers import CalculatorController


class CalculatorApp:

    def __init__(self):
        self.model = CalculatorModel()
        self.presenter = CalculatorPresenter(model)
        self.view = CalculatorView()
        self.controller = CalculatorController()

        # so the presenter... will get passed what? an interface for the model?
        # and can I set up a binding to that?
        # then what will happen? you have to update the viewmodel somehow.
        # so the view itself could have a binding and a reference to the presenter...?
        # I don't think you need a viewmodel then. right?
        #

    def start(self):
        pass