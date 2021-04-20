package demoWithRobot

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil

object HabitualMsgs {
    const val forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}"
    const val backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 350}"
    const val microStepMsg = "{\"robotmove\":\"moveForward\", \"time\": 5}"
    const val littleBackwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 10}"
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
    const val endMoveId = "endmove"
    val endMoveMsg = "{\"ID\":\"ENDMOVE\" }".replace("ID", endMoveId)
    const val executorEndId = "executorend"
    val executorendokMsg = "{\"ID\":\"ok\" }".replace("ID", executorEndId)
    val executorendkoMsg = "{\"ID\":\"PATHDONE\" }".replace("ID", executorEndId)
    const val runawyEndId = "runawayend"
    val runawyEndMsg = "{\"ID\":\"RESULT\" }".replace("ID", runawyEndId)
    const val runawyStartId = "runawaystart"
    val runawyStartMsg = "{\"ID\":\"PATHTODO\" }".replace("ID", runawyStartId)
    const val stepId = "step"
    val stepMsg = "{\"ID\":\"TIME\" }".replace("ID", stepId)
    const val stepDoneId = "stepDone"
    val stepDoneMsg = "{\"ID\":\"ok\" }".replace("ID", stepDoneId)
    const val stepFailId = "stepFail"
    val stepFailMsg = "{\"ID\":\"TIME\" }".replace("ID", stepFailId)

    val startMsgStr = "{\"start\":\"ok\" }"
    val endMsgStr = "{\"end\":\"ok\" }"


    fun stepRobot_l(caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", turnLeftMsg, "stepRobot")
    }
    fun stepRobot_r(caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "move", turnRightMsg, "stepRobot")
    }
    fun stepRobot_step(caller:String) : ApplMessage {
        return MsgUtil.buildDispatch(caller, "step", stepMsg, "stepRobot")
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
}