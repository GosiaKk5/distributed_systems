module SmartHome
{

    // Lamp
    enum Brightness {low, normal, high};
    struct Color
    {
        int red;
        int green;
        int blue;
    };

    class LampState
    {
        bool isOn;
        optional(1) Brightness brightness;
        optional(2) Color color;
        optional(3) bool autoBrightness;
        optional(4) bool isDancing;
    };

    interface Lamp
    {
        bool switchLight();
        idempotent LampState getState();
    };

    //SuperLamp
    exception LampIsInAutoModeBrightnessChangeImpossible{};

    interface SuperLamp extends Lamp
    {
        idempotent void changeBrightness(Brightness brightness) throws LampIsInAutoModeBrightnessChangeImpossible;
        idempotent void changeColor(Color color);
        void switchAutoBrightness();
    };

    //DancingLamp

    interface DancingSuperLamp extends SuperLamp
    {
        void switchDancingMode();
    };

    //Garage door
    exception ThereIsSomethingInTheWayOfTheDoor{};

    interface GarageDoor
    {
        void changeDoorPosition() throws ThereIsSomethingInTheWayOfTheDoor;
        idempotent bool isDoorOpen();
    };

    //Termohigrometr
    struct TermohigrometrState
    {
        double temperature;
        double  humidity;
    };

    interface Termohigrometr
    {
        idempotent TermohigrometrState getState();
    };

     //TermohigrometrWithHistory
     sequence<TermohigrometrState> termohigrometrHistory;

     interface TermohigrometrWithHistory extends Termohigrometr
     {
        idempotent termohigrometrHistory getTermohigrometrHistory();
     };
};