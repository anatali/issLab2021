package demoWithRobot

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil.curThread
import kotlinx.coroutines.runBlocking
import it.unibo.actor0.sysUtil.cpus
import it.unibo.robotService.ApplMsgs
import it.unibo.supports.NaiveActorKotlinObserver
import kotlinx.coroutines.delay

private val startMsgStr = "{\"start\":\"ok\" }"
private val endMsgStr = "{\"end\":\"ok\" }"
private val startMsg = MsgUtil.buildDispatch("main", "start", startMsgStr, "any")
private val endMsg = MsgUtil.buildDispatch("main", "end", endMsgStr, "any")


    fun main( ) {
        println("BEGINS CPU=$cpus ${curThread()}")
        runBlocking {
            val obs     = NaiveActorKotlinObserver("obs", 0, this)
            val myrobot = RobotExecutor("myrobot", obs)
            val cmdStr  = ApplMsgs.executorstartMsg
                .replace("PATHTODO", "wl")
                .replace(",","@")
            val cmd = MsgUtil.buildDispatch("main", ApplMsgs.executorStartId, cmdStr, "myrobot")
            myrobot.send(cmd)

            //delay(5000)

            println("ENDS runBlocking ${curThread()}")
        }
        println("ENDS main ${curThread()}")
    }


