#include "Potentiometer.h"
#include "Arduino.h"

Potentiometer::Potentiometer(int pin){
  this->pin = pin;  
};

int Potentiometer::getPot(){
  return analogRead(this->pin);  
};
