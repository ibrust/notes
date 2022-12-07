from abc import ABC, abstractmethod
from enum import Enum
from typing import Callable


class CoordinatorResult(Enum):
    pass


class BaseCoordinatorProtocol(ABC):

    # TODO: add support for an input & output event subject, tracking of child coordinators

    @abstractmethod
    def start(self):
        pass

    @abstractmethod
    def finish(self, result: CoordinatorResult):
        pass

    @property
    def onFinishCallback(self):
        """this block can be set by calling code. It executes when the coordinator finishes"""
        return self._onFinishCallback

    @onFinishCallback.setter
    def onFinishCallback(self, callback: Callable[[CoordinatorResult], None]):
        if not isinstance(callback, Callable):
            raise TypeError("property must be of type Callable")
        # TODO: - inspect the signature of callable & verify it matches the expectation...

        self._onFinishCallback = callback