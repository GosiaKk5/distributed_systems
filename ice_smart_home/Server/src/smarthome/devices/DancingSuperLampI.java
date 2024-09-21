package smarthome.devices;

import SmartHome.DancingSuperLamp;
import SmartHome.LampState;
import com.zeroc.Ice.Current;

public class DancingSuperLampI extends SuperLampI implements DancingSuperLamp {
    public DancingSuperLampI(){
        super();
        LampState lampState = getLampState();
        lampState.setIsDancing(false);
    }
    @Override
    public void switchDancingMode(Current current) {
        LampState lampState = getLampState();
        lampState.setIsDancing(!lampState.getIsDancing());
        System.out.println(current.id.name + ": Swiched dancing mode to " + lampState.getIsDancing());
    }
}
