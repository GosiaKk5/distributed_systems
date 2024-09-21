package smarthome.devices;

import SmartHome.Lamp;
import SmartHome.LampState;
import com.zeroc.Ice.Current;

public class LampI implements Lamp {
    private final LampState lampState;

    public LampI(){
        lampState = new LampState(false);
    }

    public LampState getLampState() {
        return lampState;
    }

    @Override
    public boolean switchLight(Current current) {
        lampState.isOn = !lampState.isOn;
        System.out.println(current.id.name + ": Switched light. Is it on? " + lampState.isOn);
        return lampState.isOn;
    }

    @Override
    public LampState getState(Current current) {
        System.out.println(current.id.name + ": Reported state");
        return lampState;
    }
}
