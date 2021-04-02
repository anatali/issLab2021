package it.unibo.supports

import it.unibo.supports.ActorMsgs

object ActorMsgs {
    const val startTimerId = "startTimer"
    const val endTimerId   = "endTimer"
    val startTimerMsg      = "{\"ID\":\"TIME\" }".replace("ID", startTimerId)
    val endTimerMsg        = "{\"ID\":\"ok\" }".replace("ID", endTimerId)
}