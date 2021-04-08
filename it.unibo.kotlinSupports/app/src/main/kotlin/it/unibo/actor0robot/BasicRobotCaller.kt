package it.unibo.actor0robot

import it.unibo.actor0.*
import kotlinx.coroutines.*

class BasicRobotCaller(name: String, scope: CoroutineScope) : ActorBasicKotlin( name, scope ) {

    private val basicRobot = BasicRobotActor("basicRobot"  )

    suspend fun doJob(){
        basicRobot.registerActor(this)
        val cmd    = MsgUtil.buildDispatch(name,"robotmove",
            ApplMsgs.turnLeftMsg.replace(",","@"), basicRobot.name)
        for( i in 1..4) {
            delay(1000)
            //basicRobot.waitUser("1 to send")
            println("$name | sends $cmd")
            basicRobot.send(cmd)
        }
        basicRobot.send(MsgUtil.endDefaultMsg)
    }
    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name | BasicRobotCaller $msg")
        if( msg.msgId == MsgUtil.startDefaultId){
            basicRobot.send(MsgUtil.startDefaultMsg)
            delay(1000)
            doJob()
        }
    }


}
fun main() {
    //sysUtil.trace = true
    val startTime = sysUtil.aboutSystem("BasicRobotCaller")
    println("==============================================")
    println("BasicRobotCaller | START ${sysUtil.aboutSystem("BasicRobotCaller")}");
    println("==============================================")

    runBlocking {
         val caller = BasicRobotCaller("caller", this)
         caller.send( MsgUtil.startDefaultMsg )
    }

    val endTime = sysUtil.getDuration(startTime)
    println("==============================================")
    println("BasicRobotCaller | END TIME=$endTime ${sysUtil.aboutThreads("BasicRobotCaller") }"  );
    println("==============================================")
    System.exit(1)  //quite radical, since the ws support takes time to terminate
}

