package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil.buildDispatch
import it.unibo.robotService.BasicStepRobotActor
import org.json.JSONObject

class SimpleRobotCaller(name: String ) : ActorBasicKotlin( name ) {

    private val stepMsg = "{\"step\":\"350\" }"
    private val turnLeftMsg = "{\"robotmove\":\"turnLeft\" @ \"time\": 300}"
    private val turnleft = buildDispatch("main", "move", turnLeftMsg, "stepRobot")
    private val dostep = buildDispatch("main", "step", stepMsg, "stepRobot")
    private val robot = BasicStepRobotActor(
        "stepRobot", this, scope, "localhost")

    protected fun doMoves() {
        robot.registerActor(this)
        robot.send(dostep.toString())
        //robot.send(turnleft.toString())
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if (msg.msgId == "start") {
            doMoves()
        } else if (msg.msgId == "stepAnswer") {
            val answerJson = JSONObject(msg.msgContent)
            if (answerJson.has("stepDone")) {
                robot.send(turnleft.toString())
            }
        } else if (msg.msgId == "endmove") {
            val answerJson = JSONObject(msg.msgContent.replace("@", ","))
            if (answerJson.getString("endmove") != "notallowed") {
                robot.terminate()
                terminate()
                System.exit(0 )
            }
        }
    }
}