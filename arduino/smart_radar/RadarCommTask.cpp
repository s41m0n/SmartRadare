#include "RadarComm.h"
#include "Logger.h"

extern String incomingString;

RadarComm::RadarComm() {
}

void RadarComm::init(int period) {
  Task::init(period);
}

//This task use the services offered by MsgService, checking if it is arrived a new message
//When arrives a message, it has been copied to the global string in order that the Radar Task can receive it!
void RadarComm::tick() {
  if (MsgService.isMsgAvailable()) {
    Msg* msg = MsgService.receiveMsg();
    incomingString = msg->getContent();
    delete msg;
  }
}


