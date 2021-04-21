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
        //robot.send(ApplMsgs.stepRobot_w("main", "800"))

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
                robot.send(ApplMsgs.stepRobot_l("main"))
            }else if (answerJson.has("stepFail")){
                val tback = answerJson.getString("stepFail")
                println("$name | handleInput stepFail tback=$tback")
                //robot.send(ApplMsgs.stepRobot_s("main", tback))
            }
        }
        if (msg.msgId == "endmove") {
            val answerJson = JSONObject(msg.msgContent)  //.replace("@", ","))
            if (answerJson.has("endmove")) {//&& answerJson.getString("endmove") == "notallowed"){
                val endmove = answerJson.getString("endmove")
                val move = answerJson.getString("move")
                println("endmove=${endmove} move=$move")
            }
        }
    }
}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val rc = SimpleRobotCaller("rc")

        rc.send(ApplMsgs.startAny("main"))

        //delay(5000)

        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
