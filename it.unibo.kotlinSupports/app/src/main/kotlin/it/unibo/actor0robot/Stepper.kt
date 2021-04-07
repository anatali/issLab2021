package it.unibo.actor0robot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class Stepper(name:String, scope: CoroutineScope) : ActorBasicKotlin(name, scope) {
    val stepMsg = ApplMsgs.stepMsg.replace("TIME", "350")

    var steprobotactor    : ActorBasicKotlin
    var startStepRobotMsg : ApplMessage

    init{
        steprobotactor    = StepRobotActor("stepactor", this, "localhost", scope)
        startStepRobotMsg = MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, steprobotactor.name )
        println( "$name Stepper | init ${infoThreads()}")
    }

    fun doStep(){
        //MsgUtil.sendMsg("main", ApplMsgs.stepId, ApplMsgs.stepMsg.replace("TIME", "350"), stepper)
        steprobotactor.send( startStepRobotMsg )
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name  | handleInput $msg ${sysUtil.aboutThreads(name)}");
        if( msg.msgId == "start" ) doStep()
        else if( msg.msgId == "stepAnswer" ){
            val answer = JSONObject(msg.msgContent)
            if( answer.has("stepDone")) doStep()
            else{
                println("$name  | handleInput $answer  ");
                terminate()
            }
        }
    }
}//Stepper
