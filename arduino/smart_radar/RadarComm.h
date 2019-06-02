#ifndef __RADAR_COMM__
#define __RADAR_COMM__

#include "Task.h"
#include "MsgService.h"

//Task for Communication!
class RadarComm: public Task {

  public:
    RadarComm();
    void init(int period);
    void tick();

};

#endif

