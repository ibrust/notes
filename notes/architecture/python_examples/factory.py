from abc import ABC, abstractmethod
#abc stands for abstract base class 

class InstanceType(ABC):
    @abstractmethod
    def getInstanceNumber() -> int:
        pass

class Factory():
    def makeInstance(self, arg) -> InstanceType:
        match arg:
            case 1:
                return Instance1()
            case 2:
                return Instance2()

class Instance1(InstanceType):
    def getInstanceNumber(self):
        return 1

class Instance2(InstanceType):
    def getInstanceNumber(self):
        return 2

def client_code(arg: int):
    factory = Factory()
    instance = factory.makeInstance(arg)

    num = instance.getInstanceNumber()

    print(num)


if __name__ == "__main__":
    client_code(1)
    client_code(2)