#ifndef __RADAR__
#define __RADAR__

#include "Task.h"
#include "Servo.h"
#include "Sonar.h"
#include "Led.h"
#include "Potentiometer.h"

//Pin used by his component
#define SERVO_PIN 5
#define TRIG_PIN 3
#define ECHO_PIN 4
#define CONNECTED_PIN 2
#define POT_PIN A0

//RADAR TASK!
class Radar: public Task {

  public:
    Radar();
    void init(int period);
    void tick();
  private:
    enum { REST, SCANNING } state;  //Two states: REST (OFF) and SCANNING (ON)
    bool isTracking;                //Boolean value, is set TRUE when CentralUnit says it is in tracking mode
    bool isClockwise;               //Boolean value, changed every 180 degrees.
    short int angle;                //Actual angle of the servo    
    Servo myServo;                  //Servo
    Sonar* mySonar;                 //Proximity Sensor
    Led* myConnected;               //Green Led
    Potentiometer* myPotentiometer; // Potentiometer (used to set the increment of the angle
    void scan();                    //Private method to scan and report result to the Serial
    void updateAngle();             //Private method to update the angle at every tick

};

#endif

