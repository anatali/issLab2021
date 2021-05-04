/*
BasicStepGenericCaller
Performs a call to a local or to a remote BasicStepRobot named 'stepRobot'
WARNING: check that it is the only one 'stepRobot' running
 */
package demoWithRobot

import com.andreapivetta.kolor.Color
import it.unibo.actor0.AbstractActorCaller
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.ApplMessageType
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class BasicStepGenericCaller( name: String, scope: CoroutineScope, web: Boolean  ) :
                            AbstractActorCaller( name, scope, web ) {
var lastMove = "none"
    override suspend fun handleInput(msg: ApplMessage) {
        if(msg.msgId=="sonarEvent") return
        colorPrint("$name | BasicStepGenericCaller handleInput $msg", Color.LIGHT_BLUE)
        when( msg.msgType  ){
            ApplMessageType.dispatch.toString() -> {
                val mJson = JSONObject( msg.msgContent )
                val forMe = mJson.has("robotmove") || mJson.has("step")
                if( forMe ){
                    lastMove = mJson.keys().next()
                    colorPrint("$name | BasicStepGenericCaller move=$lastMove",Color.GREEN)
                    forward( msg )  //defined in AbstractActorCaller
                }
                else { //answer from the previous call
                    //colorPrint("$name | BasicStepGenericCaller lastMove=$lastMove",Color.LIGHT_MAGENTA)
                    this.updateObservers(msg)
                }
            }  //dispatch
        }
    }
 }

//OPTIONAL ...
fun createStepRobotLocal(scope: CoroutineScope){
    val obs = NaiveObserver("obs", scope)
    BasicStepRobotActor("stepRobot",obs, scope, "localhost")
}
fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        //createStepRobotLocal(this) //WARNING: check that it is the only one running
        val caller    = BasicStepGenericCaller("rac", this, true )
        caller.send( ApplMsgs.stepRobot_step("test") )
        //caller.send( ApplMsgs.stepRobot_l("test") )
        //caller.send( ApplMsgs.stepRobot_r("test") )
        delay(1000)
        caller.terminate()
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}

