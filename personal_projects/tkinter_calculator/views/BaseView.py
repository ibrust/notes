from abc import ABC, abstractmethod


class BaseView(ABC):
    '''
    base class for use by views throughout application
    subclasses must implement constructViews() and layoutViews().
    during initialization these will automatically be called in order
    '''

    def __init_subclass__(cls, *args, **kwargs):
        super().__init_subclass__(*args, **kwargs)

        def new_init(self, *args, init=cls.__init__, **kwargs):
            init(self, *args, **kwargs)
            self.constructViews()
            self.layoutViews()

        cls.__init__ = new_init

    @abstractmethod
    def constructViews(self):
        "subclasses provide an implementation to initialize their widgets"
        pass

    @abstractmethod
    def layoutViews(self):
        "subclasses provide an implementation to arrange their widgets using tkinter's geometry managers"
        pass