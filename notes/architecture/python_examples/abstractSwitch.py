import argparse
from abc import ABC, abstractmethod
#abc stands for abstract base class 

# I think this is an outdated pattern replaced by enums, but maybe it'll be useful in certain languages still.

# so this isn't an abstract factory, it's a normal factory that instantiates an abstract type. maybe that's a kind of abstract factory, I don't know. 
# regardless the goal is to limit the number of switch statements to a single one in the application 
# the abstract case contains methods that define how a case behaves in the various places where a switch would occur

class AbstractCase(ABC):
    @abstractmethod
    def switch1behavior() -> int:
        pass 
    def switch2behavior():
        pass 

class CaseFactory():
    def makeCaseType(self, caseType) -> AbstractCase:
        match caseType:
            case 1:
                return Case1()
            case 2:
                return Case2()
            case 3:
                return Case3()

class Case1(AbstractCase):
    def switch1behavior(self) -> int:
        return 1
    def switch2behavior(self):
        print("printing - in case 1")

class Case2(AbstractCase):
    def switch1behavior(self) -> int:
        return 2
    def switch2behavior(self):
        print("printing - in case 2") 

class Case3(AbstractCase):
    def switch1behavior(self) -> int:
        return 3
    def switch2behavior(self):
        print("printing - in case 3") 


def run(caseNumber):
    caseFactory = CaseFactory()
    case = caseFactory.makeCaseType(caseNumber)

    # so this case would be any value you'd be switching off. it might be some kind of data type. in this case it's a number. 
    # you'd store this case in an object and it could be used in whatever contexts that object might encounter a switch statement. 

    print("returned number: ", case.switch1behavior())
    case.switch2behavior()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="")
    parser.add_argument("--caseNumber", action="store", dest="caseNumber", type=int, required=True)

    received_args = parser.parse_args()
    caseNumber = received_args.caseNumber 
    run(caseNumber)