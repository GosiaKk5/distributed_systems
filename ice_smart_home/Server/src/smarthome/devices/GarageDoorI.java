package smarthome.devices;

import SmartHome.GarageDoor;
import SmartHome.ThereIsSomethingInTheWayOfTheDoor;
import com.zeroc.Ice.Current;

import java.util.Random;

public class GarageDoorI implements GarageDoor {

    private boolean isOpen;
    public GarageDoorI(){
        this.isOpen = true;
    }
    @Override
    public void changeDoorPosition(Current current) throws ThereIsSomethingInTheWayOfTheDoor {
        if(isOpen){
            if(isDoorCanBeClosed()){
                System.out.println(current.id.name + ": Closed");
                isOpen = false;
            }else{
                throw new ThereIsSomethingInTheWayOfTheDoor();
            }

        }else{
            System.out.println(current.id.name + ": Opened");
            isOpen = true;
        }
    }

    @Override
    public boolean isDoorOpen(Current current) {
        System.out.println(current.id.name + ": Gave state");
        return isOpen;
    }

    private boolean isDoorCanBeClosed(){
        Random random = new Random();
        return random.nextBoolean();
    }
}
