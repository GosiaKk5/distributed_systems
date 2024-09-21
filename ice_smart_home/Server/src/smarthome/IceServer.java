package smarthome;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import smarthome.devices.LampI;
import smarthome.devices.SuperLampI;

import static com.zeroc.Ice.Util.stringToIdentity;

public class IceServer {
    public void run(String[] args)
    {
        int status = 0;
        Communicator communicator = null;
        try
        {
            communicator = Util.initialize(args);
            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter1");
            SmartHomeServantLocator smartHomeServantLocator = new SmartHomeServantLocator();
            adapter.addServantLocator(smartHomeServantLocator, "");

            adapter.activate();
            communicator.waitForShutdown();

        } catch (Exception e) {
            e.printStackTrace(System.err);
            status = 1;
        }
            if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                status = 1;
            }
        }
            System.exit(status);
        }

        public static void main(String[] args){
            IceServer iceServer = new IceServer();
            iceServer.run(args);
        }
}
