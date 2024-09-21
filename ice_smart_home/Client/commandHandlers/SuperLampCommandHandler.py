import sys, Ice
import SmartHome
from commandHandlers.LampCommandHandler import LampCommandHandler

class SuperLampCommandHandler(LampCommandHandler):
    def __init__(self, communicator, name):
        self.basePrx = communicator.propertyToProxy(name + ".Proxy")
        self.lampPrx = SmartHome.SuperLampPrx.checkedCast(self.basePrx)
        # self.lampState = self.lampPrx.getState()

    def handleCommand(self, action, args):
        super().handleCommand(action, args)

        match action:
            case "state":
                lampState = self.lampPrx.getState()
                print("Brightness: ", lampState.brightness)
                print("Color: ", lampState.color)
                print("Auto Brightness is on: ", lampState.autoBrightness)

            case "brightness":
                if len(args) < 1:
                    return "More arguments needed"
                try:
                    self.lampPrx.changeBrightness(SmartHome.Brightness.valueOf(int(args[0])))

                except SmartHome.LampIsInAutoModeBrightnessChangeImpossible:
                    print("Turn off auto mode")      

            case "color":
                if len(args) < 3:
                    return "More arguments needed"
                self.lampPrx.changeColor(SmartHome.Color(int(args[0]), int(args[1]), int(args[2])))
            
            case "auto":
                self.lampPrx.switchAutoBrightness()
