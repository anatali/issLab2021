package demoWithRobot

import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil.curThread
import kotlinx.coroutines.runBlocking
import it.unibo.actor0.sysUtil.cpus
import kotlinx.coroutines.delay

private val startMsgStr = "{\"start\":\"ok\" }"
private val endMsgStr = "{\"end\":\"ok\" }"
private val startMsg = MsgUtil.buildDispatch("main", "start", startMsgStr, "any")
private val endMsg = MsgUtil.buildDispatch("main", "end", endMsgStr, "any")


    fun main( ) {
        println("BEGINS CPU=$cpus ${curThread()}")
        runBlocking {
            val myrobot = SimpleRobotCaller("myrobot")

            myrobot.send(startMsg)

            //delay(5000)

            println("ENDS runBlocking ${curThread()}")
        }
        println("ENDS main ${curThread()}")
    }


