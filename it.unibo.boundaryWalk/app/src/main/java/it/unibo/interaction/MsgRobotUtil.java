package it.unibo.interaction;

import it.unibo.interaction.AppMsg;


public class MsgRobotUtil {

    public static final int wtime    = 400;
    public static final int stime    = wtime;
    public static final int ltime    = 300;
    public static final int rtime    = ltime;
    public static final int htime    = 100;

    public static final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 1600}";
    public static final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    public static final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";

    //msg( MSGID,  MSGTYPE,  SENDER,  RECEIVER,  CONTENT, SEQNUM )
    public static final AppMsg ahead = AppMsg.create( "msg(robotcmd,dispatch,appl,wenv,w)");
    public static final AppMsg left  = AppMsg.create( "msg(robotcmd,dispatch,appl,wenv,l)");
    public static final AppMsg right = AppMsg.create( "msg(robotcmd,dispatch,appl,wenv,r)");
    public static final AppMsg back  = AppMsg.create( "msg(robotcmd,dispatch,appl,wenv,s)");
}
