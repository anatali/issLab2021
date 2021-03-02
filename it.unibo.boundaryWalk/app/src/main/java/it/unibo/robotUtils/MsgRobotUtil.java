package it.unibo.robotUtils;

import it.unibo.interaction.AppMsg;


public class MsgRobotUtil {

    public static final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 1600}";
    public static final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    public static final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";

    //msg( MSGID,  MSGTYPE,  SENDER,  RECEIVER,  CONTENT, SEQNUM )
    public static final AppMsg ahead = AppMsg.create( "msg(goahead, dispatch, appl, wenv, w, 1)");
    public static final AppMsg left  = AppMsg.create( "msg(left, dispatch, appl, wenv, l, 2)");
    public static final AppMsg right = AppMsg.create( "msg(left, dispatch, appl, wenv, r, 3)");
    public static final AppMsg back  = AppMsg.create( "msg(goahead, dispatch, appl, wenv, s, 4)");
}
