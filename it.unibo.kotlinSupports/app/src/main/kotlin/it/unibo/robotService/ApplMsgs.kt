/*
============================================================
ApplMsgs

============================================================
*/
package it.unibo.robotService

import it.unibo.actor0.ApplMessage

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


    val demoMsgAppl =
        ApplMessage("cmd","dispatch","demo","obs","todo","1")
    //val robotcmdMsgAppl =
        //ApplMessage("robotcmd","dispatch",SENDER,RECEIVER,CMD,"1")



}