package smarthome;

import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;
import smarthome.devices.*;

import java.util.ArrayList;

public class SmartHomeServantLocator implements ServantLocator {
    private final ArrayList<String> servantsNames = new ArrayList<>();
    @Override
    public LocateResult locate(Current current) throws UserException {
        String name = current.id.name;
        ObjectAdapter adapter = current.adapter;
        Object servant = adapter.find(current.id);

        switch(name){
            case "Lamp1", "Lamp2" -> {
                servant = new LampI();
                adapter.add(servant,  new Identity(name, "Lamp"));
            }
            case "SuperLamp1", "SuperLamp2"-> {
                servant = new SuperLampI();
                adapter.add(servant,  new Identity(name, "SuperLamp"));
            }
            case "DancingSuperLamp1", "DancingSuperLamp2" -> {
                servant = new DancingSuperLampI();
                adapter.add(servant,  new Identity(name, "DancingSuperLamp"));
            }
            case "GarageDoor1", "GarageDoor2" -> {
                servant = new GarageDoorI();
                adapter.add(servant, new Identity(name, "GarageDoor"));
            }
            case "Termohigrometr1", "Termohigrometr2" -> {
                servant = new TermohigrometrI();
                adapter.add(servant, new Identity(name, "Termohigrometr"));
            }
            case "TermohigrometrWithHistory1", "TermohigrometrWithHistory2" -> {
                servant = new TermohigrometrWithHistoryI();
                adapter.add(servant, new Identity(name, "TermohigrometrWithHistory"));
            }
            default -> throw new RuntimeException("Invalid servant name");
        }

        servantsNames.add(name);
        printServantsNames();

        return new ServantLocator.LocateResult(servant, null);

    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {

    }

    @Override
    public void deactivate(String s) {

    }

    public ArrayList<String> getServantsNames(){
        return servantsNames;
    }

    public void printServantsNames(){
        System.out.print("Active servants: ");

        for(String name: servantsNames){
            System.out.print(name + " ");
        }

        System.out.println();
    }


}
