/*
BasicStepRobotActorCaller
Extends AbstractActorCaller.kt and implements either a local-call or a call via TCP.
 */
package it.unibo.actorAppl
import com.andreapivetta.kolor.Color
import it.unibo.actor0.AbstractActorCaller
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.ApplMessageType
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class BasicStepRobotActorCaller( name: String, scope: CoroutineScope,  web:Boolean=true  ) :
                            AbstractActorCaller( name, scope, web) {

    override suspend fun handleInput(msg: ApplMessage) {
        colorPrint("$name | BasicStepRobotActorCaller handleInput $msg", Color.GREEN)
        when( msg.msgType  ){
            ApplMessageType.dispatch.toString() -> {
                val mJson = JSONObject( msg.msgContent )
                val forMe = mJson.has("robotmove") || mJson.has("step")
                if( forMe ) forward( msg ) //defined in AbstractActorCaller
                else {
                    colorPrint("$name | BasicStepRobotActorCaller msg to be handled by an observer",Color.GREEN)
                    this.updateObservers(msg)
                }
            }  //dispatch
        }
    }
 }

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val rac    = BasicStepRobotActorCaller("rac", this )
        rac.send( ApplMsgs.stepRobot_w("test") )
        delay(1000)
        rac.terminate()
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
