package it.unibo.actorAppl

import it.unibo.actor0.AbstractActorCaller
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.sysUtil
import it.unibo.interaction.IUniboActor
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class ActorCallerDemo( name: String, scope: CoroutineScope  ) :
                            AbstractActorCaller( name, scope ) {

    fun doCall(){
        forward( ApplMsgs.stepRobot_l(name ) )
        forward( ApplMsgs.stepRobot_r(name ) )
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if( msg.msgId=="start") doCall()
    }
 }

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        //CONFIGURE THE SYSTEM
        val rc    = ActorCallerDemo("rc", this )

        //BasicStepRobotActor as local resource
        //val robot = BasicStepRobotActor("stepRobot", ownerActor=rc, this, "localhost")

        val startMsg = ApplMsgs.startAny("main")
        //println("main | $startMsg")
        rc.send( startMsg )

        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
