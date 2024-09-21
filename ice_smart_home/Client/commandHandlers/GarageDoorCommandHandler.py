import SmartHome

class GarageDoorCommandHandler:
    def __init__(self, communicator, name):
        self.basePrx = communicator.propertyToProxy(name + ".Proxy")
        self.garageDoorPrx = SmartHome.GarageDoorPrx.checkedCast(self.basePrx)

    def handleCommand(self, action, args):
        match action:
            case "switch":
                try:
                    self.garageDoorPrx.changeDoorPosition()
                except SmartHome.ThereIsSomethingInTheWayOfTheDoor:
                    print("There is something in the way of ther door. I can not be closed")
            case "state":
                print("Is open: ", self.garageDoorPrx.isDoorOpen())