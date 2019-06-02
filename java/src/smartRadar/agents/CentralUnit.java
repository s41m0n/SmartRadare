package consegna03.smartRadar.agents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import consegna03.smartRadar.common.BasicEventLoopController;
import consegna03.smartRadar.common.Event;
import consegna03.smartRadar.common.Serial;
import consegna03.smartRadar.devices.ButtonPressed;
import consegna03.smartRadar.devices.Light;
import consegna03.smartRadar.devices.ScanInfoReceived;
import consegna03.smartRadar.devices.ObservableButton;

public class CentralUnit extends BasicEventLoopController {

    //3 states
    private enum State {
        IDLE, SCANNING, TRACKING
    };

    private State state;
    private Light ledOn;
    private Light ledDetected;
    private Light ledTracking;
    private ObservableButton onButton;
    private ObservableButton offButton;
    private Serial serialDevice;
    private int countDetected;          //number of detected in the current trip
    private int currentAngle;           //current angle (updated at last info received)
    private double currentDistance;     //current distance (updated at last info received)
    private Calendar calendar = Calendar.getInstance(); //Instance of calendar
    private boolean restarted;          //boolean value set at true when trip is finished
    private String fileName;            //the name of the file to save data

    private static final double TRACKING_DIST = 0.2; //Default distance for tracking
    private static final double DETECT_DIST = 0.5;   //Default distance for detecting
    private static final String MSG_OFF = "OFF";     //Message to send to Arduino ON
    private static final String MSG_ON = "ON";       //Message to send to Arduino OFF
    private static final String MSG_TRACKING = "TRACKING"; //Message to send to Arduino TRACKING
    private static final String MSG_NO_MORE_TRACKING = "CONTINUE";  //Message to send to Arduino STOP TRACKING
    private static final int MAX_ANGLE = 180;        //Max angle reached by servo
    private static final int MIN_ANGLE = 0;          //Min angle reached by servo
    private static final int FLASH_TIME = 100;       //Time for detect led

    //Constructor!
    //@param
    //  fileName -> the name of the file
    //  ledOn,ledDetected,ledTracking -> the 3 leds
    //  on,off -> The two button
    //  serialDevice -> The Serial instance
    public CentralUnit(String fileName, Light ledOn, Light ledDetected, Light ledTracking, ObservableButton on,
            ObservableButton off, Serial serialDevice) {
        this.fileName = fileName;
        this.ledOn = ledOn;
        this.ledDetected = ledDetected;
        this.ledTracking = ledTracking;
        this.onButton = on;
        this.offButton = off;
        this.state = State.IDLE;
        this.serialDevice = serialDevice;
        this.onButton.addObserver(this);
        this.offButton.addObserver(this);
        this.currentAngle = 0;
        this.currentDistance = 0;
        this.restarted = true;
    }

    @Override
    protected void processEvent(Event ev) {
        try {
            //Check if we're not in IDLE and btnOff is pressed.
            if (ev instanceof ButtonPressed && ((ButtonPressed) ev).getSourceButton().equals(this.offButton)
                    && this.state != State.IDLE) {
                this.state = State.IDLE;
                serialDevice.sendMsg(MSG_OFF);
                this.ledOn.switchOff();
                this.ledDetected.switchOff();
                this.ledTracking.switchOff();
                return;
            }
            switch (state) {
            case IDLE: {
                //Waiting for btnOn pressed
                if (ev instanceof ButtonPressed && ((ButtonPressed) ev).getSourceButton().equals(this.onButton)) {
                    this.state = State.SCANNING;
                    this.ledOn.switchOn();
                    serialDevice.sendMsg(MSG_ON);
                }
                break;
            }
            case SCANNING: {
                //If messageReceived, check distance, check detected and print info
                if (ev instanceof ScanInfoReceived) {
                    this.parseInfo(((ScanInfoReceived) ev).getMessage());
                    if (this.currentDistance <= TRACKING_DIST) {
                        this.state = State.TRACKING;
                        serialDevice.sendMsg(MSG_TRACKING);
                        this.ledTracking.switchOn();
                    } else if (this.currentDistance <= DETECT_DIST) {
                        this.countDetected++;
                        this.printDetectInfo();
                        this.ledDetected.switchOn();
                        Thread.sleep(FLASH_TIME);
                        this.ledDetected.switchOff();
                    }
                    if ((this.currentAngle == MIN_ANGLE || this.currentAngle == MAX_ANGLE) && !this.restarted) {
                        System.out.println(
                                "Time " + this.getTime() + " - Finish trip! Objects detected: " + this.countDetected);
                        this.countDetected = 0;
                    }
                }
                break;
            }
            case TRACKING: {
                //If messReceived, check distance and print info
                if (ev instanceof ScanInfoReceived) {
                    this.parseInfo(((ScanInfoReceived) ev).getMessage());
                    if (this.currentDistance >= TRACKING_DIST) {
                        this.state = State.SCANNING;
                        serialDevice.sendMsg(MSG_NO_MORE_TRACKING);
                        this.ledTracking.switchOff();
                    } else {
                        this.printTrackingInfo();
                    }
                }
                break;
            }
            default: {
                break;
            }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //Method to parse the message received (composed by "Angle Distance")
    private void parseInfo(String msg) {
        String[] msgParsed = msg.split(" ");
        int prevAngle = this.currentAngle;
        this.currentAngle = Integer.parseInt(msgParsed[0]);
        //Check if it has finished the trip
        if (prevAngle == this.currentAngle)
            this.restarted = true;
        else
            this.restarted = false;
        this.currentDistance = Double.parseDouble(msgParsed[1]);
    }

    // Method used to print in standard output + file the Scanning Info
    private void printDetectInfo() {
        String msgToPrint = "Time " + this.getTime() + " - Object detected at angle " + this.currentAngle;
        System.out.println(msgToPrint);
        // In this part that string is also appended to the file
        try (PrintWriter out = new PrintWriter(new FileOutputStream(new File(this.fileName), true))) {
            out.append(msgToPrint + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method used to print in standard output the Tracking Info
    private void printTrackingInfo() {
        System.out.println("Time " + this.getTime() + " - Object tracked at angle " + this.currentAngle + " - distance "
                + this.currentDistance);
    }
    
    //Method to return current Date time
    private String getTime() {
        return this.calendar.getTime().toString();
    }
}
