package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil.buildDispatch
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class SimpleRobotCaller(name: String ) : ActorBasicKotlin( name ) {
/*
    private val stepMsg = "{\"step\":\"350\" }"
    private val turnLeftMsg = "{\"robotmove\":\"turnLeft\" @ \"time\": 300}"
    private val turnleft = buildDispatch("main", "move", turnLeftMsg, "stepRobot")
    private val dostep = buildDispatch("main", "step", stepMsg, "stepRobot")
*/
    private val robot = BasicStepRobotActor("stepRobot", this, scope, "localhost")

    protected fun doMoves() {
        robot.registerActor(this)
        robot.send(ApplMsgs.stepRobot_step("main", "350"))
        //robot.send(turnleft.toString())
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if (msg.msgId == "start") {
            doMoves()
        }
        if (msg.msgId == "stepAnswer") {
            val answerJson = JSONObject(msg.msgContent)
            if (answerJson.has("stepDone")) {
                robot.send( ApplMsgs.stepRobot_l("main") )
            }
        }
        if (msg.msgId == "endmove") {
            val answerJson = JSONObject(msg.msgContent.replace("@", ","))
            if (answerJson.getString("endmove") != "notallowed") {
                robot.terminate()
                terminate()
                System.exit(0 )
            }
        }
    }
}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val myrobot = SimpleRobotCaller("myrobot")

        myrobot.send(ApplMsgs.startAny("main"))

        //delay(5000)

        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
