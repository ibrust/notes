from threading import Lock, Thread


class SingletonMetaclass(type):
    _instances = {}
    _lock: Lock = Lock()                    # singletons must be thread safe - different threads could get separate instances otherwise

    def __call__(cls, *args, **kwargs):     # apparently this ensures any changes to __init__ won't effect the returned instance 
        with cls._lock:
            if cls not in cls._instances:
                instance = super().__call__(*args, **kwargs)
                cls._instances[cls] = instance
        return cls._instances[cls]

class Singleton(metaclass=SingletonMetaclass):
    value: str = None
    def __init__(self, value: str) -> None:
        self.value = value

    def singletonMethod1(self):
        pass


def test_singleton(value: str) -> None:
    singleton = Singleton(value)
    print(singleton.value)

if __name__ == "__main__":
    process1 = Thread(target=test_singleton, args=("1",))                   # strange syntax with the comma here
    process2 = Thread(target=test_singleton, args=("2",))

    # seeing the same value print twice shows success - the singleton was initialized once and reused
    process1.start()
    process2.start()