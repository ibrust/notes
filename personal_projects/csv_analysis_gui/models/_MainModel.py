from reactivex.subject.subject import Subject
from ..controllers import MainControllerDelegate
from ..helpers import reactiveProperty

__all__ = ['MainModel']

class MainModel(MainControllerDelegate):

    class Data:
        publisher: Subject
        def __init__(self):
            self.publisher = Subject()

    publisher: Subject
    data: Data

    def __init__(self):
        self.publisher = Subject()
        self.data = MainModel.Data()

        self.data.publisher.subscribe(
            on_next=lambda v: self.publisher.on_next(self.data)
        )


