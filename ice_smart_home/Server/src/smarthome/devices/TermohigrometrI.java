package smarthome.devices;

import SmartHome.Termohigrometr;
import SmartHome.TermohigrometrState;
import com.zeroc.Ice.Current;

import java.util.Random;

public class TermohigrometrI implements Termohigrometr {

    private TermohigrometrState termohigrometrState;

    public TermohigrometrI(){
        measureTemperatureAndHumidity();
    }

    @Override
    public TermohigrometrState getState(Current current) {
        measureTemperatureAndHumidity();
        System.out.println(current.id.name + ": Reported state");
        return termohigrometrState;
    }

    protected void measureTemperatureAndHumidity(){
        Random random = new Random();
        double temperature = random.nextDouble() * 200 - 100;
        double humidity = random.nextDouble();
        termohigrometrState = new TermohigrometrState(temperature, humidity);
    }

    public TermohigrometrState getState(){
        return termohigrometrState;
    }
}
