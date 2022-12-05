from abc import ABC, abstractmethod
#abc stands for abstract base class 

class InstanceType(ABC):
    @abstractmethod
    def getInstanceNumber() -> int:
        pass 

class AbstractFactory(ABC):
    @abstractmethod
    def makeInstance(self, arg) -> InstanceType:
        pass

class ConcreteFactory1(AbstractFactory):
    def makeInstance(self, arg) -> InstanceType:
        match arg:
            case 1:
                return Instance1()
            case 2:
                return Instance2()

class ConcreteFactory2(AbstractFactory):
    def makeInstance(self, arg) -> InstanceType:
        match arg:
            case 1:
                return Instance3()
            case 2:
                return Instance4()

class Instance1(InstanceType):
    def getInstanceNumber(self):
        return 1

class Instance2(InstanceType):
    def getInstanceNumber(self):
        return 2

class Instance3(InstanceType):
    def getInstanceNumber(self):
        return 3

class Instance4(InstanceType):
    def getInstanceNumber(self):
        return 4


def client_code(factory: AbstractFactory):
    instancea = factory.makeInstance(1)
    instanceb = factory.makeInstance(2)

    numa = instancea.getInstanceNumber()
    numb = instanceb.getInstanceNumber()

    print(numa)
    print(numb)


if __name__ == "__main__":
    client_code(ConcreteFactory1())
    client_code(ConcreteFactory2())