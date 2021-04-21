/*
============================================================
ApplMsgs

============================================================
*/
package it.unibo.robotService

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil

/*
{"robotmove":"MOVE", "time":T}
{"endmove":"RESULT", "move":MOVE}
{ "sonarName": "sonarName", "distance": 1, "axis": "x" }
{ "collision" : "false", "move": "moveForward"}
 */

object ApplMsgs {
    const val forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}"
    const val backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 350}"
    const val microStepMsg = "{\"robotmove\":\"moveForward\", \"time\": 5}"
    const val littleBackwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 5}"
    const val turnLeftMsg = "{\"robotmove\":\"turnLeft\", \"time\": 300}"
    const val turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}"
    const val haltMsg = "{\"robotmove\":\"alarm\", \"time\": 20}"
    const val goBackMsg = "{\"goBack\":\"goBack\" }"
    const val resumeMsg = "{\"resume\":\"resume\" }"
    const val forwardstepMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}"

    const val activateId = "activate"
    val activateMsg = "{\"ID\":\"ARGS\" }".replace("ID", activateId)

    const val executorStartId = "executorstart"
    val executorstartMsg = "{\"ID\":\"PATHTODO\" }".replace("ID", executorStartId)
    const val executorDoneId = "executorDone"
    const val executorFailId = "executorFail"
    val executorendokMsg = "{\"ID\":\"ok\" }".replace("ID", executorDoneId)
    val executorendkoMsg = "{\"ID\":\"PATHDONE\" }".replace("ID", executorFailId)

    const val robotMoveId     = "robotmove"
    const val robotMoveTimeId = "time"
    val robotMoveMsg = "{\"MOVEID\":\"MOVE\",\"TIMEID\":TIME}"
        .replace("MOVEID", robotMoveId).replace("TIMEID", robotMoveTimeId)

    const val endMoveId = "endmove"
    val endMoveMsg = "{\"ID\":\"ENDMOVE\" }".replace("ID", endMoveId)
    const val executorEndId = "executorend"

    //
    const val runawyEndId = "runawayend"
    val runawyEndMsg = "{\"ID\":\"RESULT\" }".replace("ID", runawyEndId)
    const val runawyStartId = "runawaystart"
    val runawyStartMsg = "{\"ID\":\"PATHTODO\" }".replace("ID", runawyStartId)

    //
    const val stepId = "step"
    val stepMsg = "{\"ID\":\"TIME\" }".replace("ID", stepId)
    const val stepDoneId = "stepDone"
    val stepDoneMsg = "{\"ID\":\"ok\" }".replace("ID", stepDoneId)
    const val stepFailId = "stepFail"
    val stepFailMsg = "{\"ID\":\"TIME\" }".replace("ID", stepFailId)

    const val robotMovecmdId = "robotmovecmd"
    val robotMovecmdMsg = "{\"ID\":\"MOVE\" }".replace("ID", robotMovecmdId)


    val startMsgStr = "{\"start\":\"ok\" }"
    val endMsgStr = "{\"end\":\"ok\" }"


    fun stepRobot_l(caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", turnLeftMsg, "stepRobot")
    }
    fun stepRobot_r(caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", turnRightMsg, "stepRobot")
    }
    fun stepRobot_turn(caller:String, dir: String="r") : ApplMessage {
        val turnMsg = if (dir=="l")  turnLeftMsg else turnRightMsg //no ternary op in kotlin
        return MsgUtil.buildDispatch(caller, "move", turnMsg, "stepRobot")
    }
    fun stepRobot_step(caller:String, time:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "step", stepMsg.replace("TIME",time), "stepRobot")
    }

    fun startAny( caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "start", startMsgStr, "any")
    }
    fun endAny( caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "start", endMsgStr, "any")
    }

    fun executorOkEnd( caller:String ) :  ApplMessage {
        return MsgUtil.buildDispatch(caller, executorEndId, executorendokMsg, "any")
    }
    fun executorFailEnd( caller:String, pathDone: String ) :  ApplMessage {
        val answer = executorendkoMsg.replace("PATHDONE", pathDone)
        return MsgUtil.buildDispatch(caller, executorEndId, answer, "any")
    }

    fun testCreateMsg( ) :  ApplMessage {
        return MsgUtil.buildDispatch("tester", "cmd", robotMoveMsg, "any")
    }



}