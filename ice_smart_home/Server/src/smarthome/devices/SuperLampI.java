package smarthome.devices;

import SmartHome.*;
import com.zeroc.Ice.Current;

public class SuperLampI extends LampI implements SuperLamp {

    public SuperLampI(){
        super();
        LampState lampState = getLampState();
        lampState.setBrightness(Brightness.normal);
        lampState.setColor(new Color(255,255,255));
        lampState.setAutoBrightness(false);

    }

    @Override
    public void changeBrightness(Brightness brightness, Current current) throws LampIsInAutoModeBrightnessChangeImpossible {
        LampState lampState = getLampState();
        if (lampState.getAutoBrightness()){
            throw new LampIsInAutoModeBrightnessChangeImpossible();
        }
        lampState.setBrightness(brightness);
        System.out.println(current.id.name + ": Changed Brightness");
    }

    @Override
    public void changeColor(Color color, Current current) {
        LampState lampState = getLampState();
        lampState.setColor(color);
        System.out.println(current.id.name + ": Changed color");
    }

    @Override
    public void switchAutoBrightness(Current current) {
        LampState lampState = getLampState();
        lampState.setAutoBrightness(!lampState.getAutoBrightness());
        System.out.println(current.id.name + ": Switched Brightness");
    }
}
