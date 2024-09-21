import SmartHome

class TermohigrometrCommandHandler:
    def __init__(self, communicator, name):
        self.basePrx = communicator.propertyToProxy(name + ".Proxy")
        self.termohigrometrPrx = SmartHome.TermohigrometrPrx.checkedCast(self.basePrx)

    def handleCommand(self, action, args):
        match action:
            case "state":
                termohigrometrState = self.termohigrometrPrx.getState()
                print("Temperature: ", termohigrometrState.temperature, "C")
                print("Humidity: ", termohigrometrState.humidity, "%")