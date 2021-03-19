package it.unibo.interaction

object MsgRobotUtil {
    //movetimes (used by IssAnnotationUtil.fillMap)
    const val wtime = 400
    const val stime = wtime
    const val ltime = 300
    const val rtime = ltime
    const val htime = 100

    //cril
    const val forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 400}"
    const val backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 400}"
    const val turnLeftMsg = "{\"robotmove\":\"turnLeft\", \"time\": 300}"
    const val turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}"
    const val haltMsg = "{\"robotmove\":\"alarm\", \"time\": 100}"

    //aril
    const val wMsg = "w"
    const val lMsg = "l"
    const val rMsg = "r"
    const val sMsg = "s"
    const val hMsg = "h" //msg( MSGID,  MSGTYPE,  SENDER,  RECEIVER,  CONTENT, SEQNUM )
 }