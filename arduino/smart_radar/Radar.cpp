#include "Arduino.h"
#include "Radar.h"
#include "Logger.h"

//Global string used to communicate
extern String incomingString;

//Initializing every component at their initial state
Radar::Radar() {
  this->state = REST;
  this->angle = 90;
  this->isTracking = false;
  this->isClockwise = true;
  myServo.attach(SERVO_PIN);
  myServo.write(this->angle);
  myPotentiometer = new Potentiometer(POT_PIN);
  mySonar = new Sonar(ECHO_PIN, TRIG_PIN);
  myConnected = new Led(CONNECTED_PIN);
}

//Initializing task with the period set in the main class
void Radar::init(int period) {
  Task::init(period);
}

//Method to update the servo angle
void Radar::updateAngle() {
  //Mapping the potentiometer value between 1 to 10 and then update angle with that value
  int increment = map(myPotentiometer->getPot(),0,1023,1,10);
  this->isClockwise ? this->angle += increment : this->angle -= increment;
  if (this->angle > 180) {
    this->angle = 180;
    this->isClockwise = false;
  } else if (this->angle < 0) {
    this->angle = 0;
    this->isClockwise = true;
  }
  myServo.write(this->angle);
}


void Radar::tick() {
  switch (state) {
    //In this state the only thing this task has to do is checking if it has received "ON" by the CentralUnit
    case REST: {
        if (incomingString == "ON") {
          state = SCANNING;
          myConnected->switchOn();
        }
        break;
      }
    //In this state the Radar keep on scanning the Area, checking if CentralUnit has communicates something to him
    //If CU says to switch off, then the state return to REST
    //If CU says tracking, then the only things that it changes is that the servo must not move
    //If CU says to continue scanning, then the servo can move again
    case SCANNING: {
        if (incomingString == "OFF") {
          state = REST;
          this->isTracking = false;
          this->angle = 90;
          myServo.write(this->angle);
          myConnected->switchOff();
          break;
        } else if (incomingString == "TRACKING") {
          this->isTracking = true;
        } else if(incomingString == "CONTINUE") {
          this->isTracking = false;
        }
        String toSend = "";
        toSend += this->angle;
        toSend += " ";
        toSend += mySonar->getDistance();
        Logger.log(toSend);
        if (this->isTracking == false) updateAngle();
        break;
      }
    default : {
        break;
      }
  }
}
