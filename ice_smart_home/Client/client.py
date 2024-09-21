import sys, Ice

import SmartHome
from commandHandlers.LampCommandHandler import LampCommandHandler
from commandHandlers.SuperLampCommandHandler import SuperLampCommandHandler
from commandHandlers.DancingSuperLampCommandHandler import DancingSuperLampCommandHandler
from commandHandlers.GarageDoorCommandHandler import GarageDoorCommandHandler
from commandHandlers.TermohigrometrCommandHandler import TermohigrometrCommandHandler
from commandHandlers.TermohigrometrWithHistoryCommandHandler import TermohigrometrWithHistoryCommandHandler

AVAILABLE_DEVICES = ["Lamp1_1", "Lamp1_2", "SuperLamp1_1", "DancingSuperLamp1_1", "GarageDoor1_1", "Termohigrometr1_1", "TermohigrometrWithHistory1_1",
                     "Lamp2_1", "SuperLamp2_1", "DancingSuperLamp2_1", "GarageDoor2_1", "Termohigrometr2_1", "TermohigrometrWithHistory2_1"]

class CommandHandlersManager:
    def __init__(self, communicator):
        self.communicator = communicator
        self.command_handlers = {}
    

    def get_commnad_handler(self, device):
        if device not in self.command_handlers.keys():
            self.command_handlers[device] = self.create_command_handler(device)

        return self.command_handlers[device]
                

    def create_command_handler(self, device):
        match device:
            case "Lamp1_1" | "Lamp2_1" | "Lamp1_2":
                return LampCommandHandler(self.communicator, device)
            case "SuperLamp1_1" | "SuperLamp2_1":
                return SuperLampCommandHandler(self.communicator, device)
            case "DancingSuperLamp1_1" | "DancingSuperLamp2_1":
                return DancingSuperLampCommandHandler(self.communicator, device)
            case "GarageDoor1_1" | "GarageDoor2_1":
                return GarageDoorCommandHandler(self.communicator, device)
            case "Termohigrometr1_1" | "Termohigrometr2_1":
                return TermohigrometrCommandHandler(self.communicator, device)
            case "TermohigrometrWithHistory1_1" | "TermohigrometrWithHistory2_1":
                return TermohigrometrWithHistoryCommandHandler(self.communicator, device)



if __name__ == "__main__":

    with Ice.initialize(['--Ice.Config=config.client'] + sys.argv) as communicator:
        
        selected_device = None

        command_handlers_manager = CommandHandlersManager(communicator)
        
        while True:
            if selected_device == None:
                command = input("Select device: ")

                if command in AVAILABLE_DEVICES:
                    selected_device = command
                    command_handler = command_handlers_manager.get_commnad_handler(selected_device)
               
            else:
                command = input(selected_device + "> ")

                command_parts = command.split()
                if len(command_parts) == 0:
                    continue 

                action = command_parts[0]
                args = command_parts[1:]

                if action == "exit":
                    selected_device = None
                else:
                    command_handler.handleCommand(action, args)



