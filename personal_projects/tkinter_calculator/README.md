simple calculator program 

the MD button changes the mode of the calculator between Binary, Decimal, and Hex.

to run: python3 -m tkinter_calculator

built using tkinter and MVVMC architecture
the data flow is: CalculatorModel > CalculatorPresenter > CalculatorView > CalculatorController
CalculatorCoordinator sits above the data flow & sets it up / manages it
CalculatorModel.Data is where the data that drives updates is located 
_ReactiveProperty.py contains a function used to create data bindings in CalculatorModel.Data
