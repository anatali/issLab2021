package demoWithRobot

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

class BasicStepGenericCaller( name: String, scope: CoroutineScope  ) :
                            AbstractActorCaller( name, scope ) {
var lastMove = "none"
    override suspend fun handleInput(msg: ApplMessage) {
        colorPrint("$name | BasicStepGenericCaller handleInput $msg", Color.LIGHT_BLUE)
        when( msg.msgType  ){
            ApplMessageType.dispatch.toString() -> {
                val mJson = JSONObject( msg.msgContent )
                val forMe = mJson.has("robotmove") || mJson.has("step")
                if( forMe ){
                    lastMove = mJson.keys().next()
                    colorPrint("$name | BasicStepGenericCaller move=$lastMove",Color.GREEN)
                    forward( msg )
                }
                else { //answer from the previous call
                    colorPrint("$name | BasicStepGenericCaller lastMove=$lastMove",Color.LIGHT_MAGENTA)
                    this.updateObservers(msg)
                }
            }  //dispatch
        }
    }
 }

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val rac    = BasicStepGenericCaller("rac", this )
        rac.send( ApplMsgs.stepRobot_step("test") )
        delay(1000)
        rac.terminate()
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}

