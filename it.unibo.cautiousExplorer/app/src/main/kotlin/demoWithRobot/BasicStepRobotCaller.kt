/*
BasicStepRobotCaller
Performs a call to a local BasicStepRobot named 'stepRobot'
 */
package demoWithRobot

import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class BasicStepRobotCaller(name: String ) : ActorBasicKotlin( name ) {

    private var robot : BasicStepRobotActor
    //private val obs   : NaiveObserverActorKotlin

    init{
        robot = BasicStepRobotActor("stepRobot", ownerActor=this, scope, "localhost")
        //obs   = NaiveObserverActorKotlin("obs", scope )
        //robot.registerActor(obs)
    }

    protected fun doMoves() {
        //robot.send(ApplMsgs.stepRobot_l("main" ))
        //robot.send(ApplMsgs.stepRobot_r("main" ))
        robot.send(ApplMsgs.stepRobot_step("main", "350"))
        //robot.send(ApplMsgs.stepRobot_l("main" ))
        //robot.send(ApplMsgs.stepRobot_step("main", "350"))
    /*
        robot.send(ApplMsgs.stepRobot_l("main" ))
        robot.send(ApplMsgs.stepRobot_l("main" ))
        robot.send(ApplMsgs.stepRobot_step("main", "350"))
        robot.send(ApplMsgs.stepRobot_l("main" ))
        robot.send(ApplMsgs.stepRobot_l("main" ))
*/
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if (msg.msgId == "start") {
            doMoves()
        }
        if (msg.msgId == "stepAnswer") {
            val answerJson = JSONObject(msg.msgContent)
            if (answerJson.has("stepDone")) {
                //robot.send(ApplMsgs.stepRobot_l("main"))
            }else if (answerJson.has("stepFail")){
                //val tback = answerJson.getString("stepFail")
                //println("$name | handleInput stepFail tback=$tback")
                robot.send(ApplMsgs.stepRobot_l("main"))
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
        sysUtil.colorPrint("WARNING: another BasicStepRobotActor could be running", Color.RED)
        val rc = BasicStepRobotCaller("rc")

        //println("main | ${ApplMsgs.stepRobot_l("main" )}")
        //println("main | ${ApplMsgs.stepRobot_step("main", "350" )}")

        val startMsg = ApplMsgs.startAny("main")
        println("main | $startMsg")
        rc.send( startMsg )

        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
