#include "Scheduler.h"
#include "Radar.h"
#include "MsgService.h"
#include "RadarComm.h"

//Time for tick and scheduler
#define TASK_RADAR 100
#define TASK_COMM 50
#define SCHEDULER_TIME 50

//Global string used to communicate between tasks
String incomingString = "";

Scheduler sched;

void setup() {
  //Initializing scheduler
  sched.init(SCHEDULER_TIME);

  //Initializing MsgService
  MsgService.init();

  //Creating the two tasks
  Radar* radar = new Radar();
  radar->init(TASK_RADAR);

  RadarComm* comm = new RadarComm();
  comm->init(TASK_COMM);

  //Adding this two tasks to the scheduler
  sched.addTask(radar);
  sched.addTask(comm);
}

void loop() {
  sched.schedule();
}
