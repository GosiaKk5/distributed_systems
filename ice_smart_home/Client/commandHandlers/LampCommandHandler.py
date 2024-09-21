import SmartHome

class LampCommandHandler:
    def __init__(self, communicator, name):
        self.basePrx = communicator.propertyToProxy(name + ".Proxy")
        self.lampPrx = SmartHome.LampPrx.checkedCast(self.basePrx)
        # self.lampState = self.lampPrx.getState()

    def handleCommand(self, action, args):
        match action:
            case "switch":
                self.lampPrx.switchLight()
            case "state":
                lampState = self.lampPrx.getState()
                print("Is on: ", lampState.isOn)
