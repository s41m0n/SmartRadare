package consegna03.smartRadar;

import consegna03.smartRadar.agents.CentralUnit;

import consegna03.smartRadar.agents.InputMsgReceiver;
import consegna03.smartRadar.common.Serial;
import consegna03.smartRadar.devices.ButtonImpl;
import consegna03.smartRadar.devices.Led;
import consegna03.smartRadar.devices.Light;
import consegna03.smartRadar.devices.ObservableButton;
import consegna03.smartRadar.devices.SerialImpl;

//Main class where all components are instantiated!
public class SmartRadarSystem {
    
    private static final int LED_ON = 0;
    private static final int LED_OFF = 2;
    private static final int LED_TRACKING = 3;
    private static final int BTN_ON = 4;
    private static final int BTN_OFF = 5;
    private static final int BOUND_RATE = 9600;
    
    public static void main(String args[]) throws Exception {
        Light ledOn = new Led(LED_ON);
        Light ledOff = new Led(LED_OFF);
        Light ledTracking = new Led(LED_TRACKING);
        ObservableButton btnOn = new ButtonImpl(BTN_ON);
        ObservableButton btnOff = new ButtonImpl(BTN_OFF);
        Serial serialDevice = new SerialImpl(args[0], BOUND_RATE);
        //Creating the two agent and then starting them!
        CentralUnit cu = new CentralUnit("log.txt", ledOn, ledOff, ledTracking, btnOn, btnOff, serialDevice);
        InputMsgReceiver rec = new InputMsgReceiver(serialDevice, cu);
        cu.start();
        rec.start();
    }
}
