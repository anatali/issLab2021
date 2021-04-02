package it.unibo.executors

import it.unibo.executors.ApplMsgs

object ApplMsgs {
    const val forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}"
    const val backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 350}"
    const val microStepMsg = "{\"robotmove\":\"moveForward\", \"time\": 10}"
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



}