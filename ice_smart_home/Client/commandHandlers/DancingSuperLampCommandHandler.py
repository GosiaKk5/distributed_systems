import SmartHome
from commandHandlers.SuperLampCommandHandler import SuperLampCommandHandler

class DancingSuperLampCommandHandler(SuperLampCommandHandler):
    def __init__(self, communicator, name):
        self.basePrx = communicator.propertyToProxy(name + ".Proxy")
        self.lampPrx = SmartHome.DancingSuperLampPrx.checkedCast(self.basePrx)

    def handleCommand(self, action, args):
        super().handleCommand(action, args)

        match action:
            case "state":
                lampState = self.lampPrx.getState()
                print("Is Dancing: ", lampState.isDancing)

            case "dance":
                self.lampPrx.switchDancingMode()
