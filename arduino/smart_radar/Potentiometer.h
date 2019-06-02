#ifndef __POTENTIOMETER__
#define __POTENTIOMETER__

class Potentiometer{
  public:
    Potentiometer(int pin);
    int getPot();
  protected:
    int pin;
};

#endif

