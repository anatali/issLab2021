package it.unibo.actorAppl

import it.unibo.actor0.AbstractActorCaller
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.ApplMessageType
import it.unibo.actor0.sysUtil
import it.unibo.interaction.IUniboActor
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class BasicStepRobotActorCaller( name: String, scope: CoroutineScope  ) :
                            AbstractActorCaller( name, scope ) {

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        when( msg.msgType ){
            ApplMessageType.dispatch.toString() -> forward( msg )
        }
    }
 }

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val rac    = BasicStepRobotActorCaller("rac", this )
        rac.send( ApplMsgs.stepRobot_l("test") )
        delay(1000)
        rac.terminate()
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
