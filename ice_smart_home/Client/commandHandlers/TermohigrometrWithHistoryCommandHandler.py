import SmartHome
from commandHandlers.TermohigrometrCommandHandler import TermohigrometrCommandHandler

class TermohigrometrWithHistoryCommandHandler(TermohigrometrCommandHandler):
    def __init__(self, communicator, name):
        self.basePrx = communicator.propertyToProxy(name + ".Proxy")
        self.termohigrometrPrx = SmartHome.TermohigrometrWithHistoryPrx.checkedCast(self.basePrx)

    def handleCommand(self, action, args):
        match action:
            case "state":
                termohigrometrState = self.termohigrometrPrx.getState()
                print("Temperature: ", termohigrometrState.temperature, "C")
                print("Humidity: ", termohigrometrState.humidity, "%")
            case "history":
                termohigrometrHistory = self.termohigrometrPrx.getTermohigrometrHistory()
                for state in termohigrometrHistory:
                    print(f"Temp: {state.temperature}, Humidity: {state.humidity}")