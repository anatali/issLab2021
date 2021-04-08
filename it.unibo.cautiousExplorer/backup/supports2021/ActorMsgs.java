package it.unibo.supports2021;

public class ActorMsgs {
    final static public String startTimerId      = "startTimer";
    final static public String endTimerId        = "endTimer";
    final static public String startTimerMsg     = "{\"ID\":\"TIME\" }".replace("ID", startTimerId);
    final static public String endTimerMsg       = "{\"ID\":\"ok\" }".replace("ID", endTimerId);

}
