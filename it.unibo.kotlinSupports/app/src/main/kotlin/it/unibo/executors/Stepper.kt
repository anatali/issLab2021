package it.unibo.executors

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class Stepper(name:String, scope: CoroutineScope) : ActorBasicKotlin(name, scope) {
    val stepMsg = ApplMsgs.stepMsg.replace("TIME", "350")

    var stepper      : ActorBasicKotlin
    var startStepMsg : ApplMessage

    init{
        stepper = StepRobotActor("stepper", this, scope)
        startStepMsg =
                MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, stepper.name )
        println( "$name Stepper | init ${infoThreads()}")
    }

    fun doStep(){
        //MsgUtil.sendMsg("main", ApplMsgs.stepId, ApplMsgs.stepMsg.replace("TIME", "350"), stepper)
        stepper.send( startStepMsg )
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name  | handleInput $msg ${sysUtil.aboutThreads(name)}");
        if( msg.msgId == "start" ) doStep()
        else if( msg.msgId == "stepAnswer" ){
            val answer = JSONObject(msg.msgContent)
            if( answer.has("stepDone")) doStep()
        }
    }
}//Stepper
