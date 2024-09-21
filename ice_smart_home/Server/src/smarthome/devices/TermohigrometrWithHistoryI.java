package smarthome.devices;

import SmartHome.Termohigrometr;
import SmartHome.TermohigrometrState;
import SmartHome.TermohigrometrWithHistory;
import com.zeroc.Ice.Current;

import java.util.ArrayList;

public class TermohigrometrWithHistoryI extends TermohigrometrI implements TermohigrometrWithHistory {

    private final ArrayList<TermohigrometrState> termohigrometrStatesHistory;

    public TermohigrometrWithHistoryI(){
        super();
        termohigrometrStatesHistory = new ArrayList<>();
    }
    @Override
    public TermohigrometrState[] getTermohigrometrHistory(Current current) {
        System.out.println(current.id.name + ": Reported history");
        return termohigrometrStatesHistory.toArray(new TermohigrometrState[0]);
    }

    @Override
    public TermohigrometrState getState(Current current) {
        termohigrometrStatesHistory.add(this.getState());
        return super.getState(current);
    }
}
