/*
============================================================
AbstractRobotActor

============================================================
*/
package it.unibo.robotService

import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import it.unibo.supports.IssWsHttpKotlinSupport
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
abstract class AbstractRobotActor(name: String, val wenvAddr: String,
                                  //scope: CoroutineScope= GlobalScope)
         scope: CoroutineScope= CoroutineScope( newSingleThreadContext("single_$name") ))
                                  : ActorBasicKotlin(name, scope, DispatchType.single) {
    protected var moveInterval = 500L //to avoid too-rapid movement
    protected var cnsl = System.console() //returns null in an online IDE
    protected val MoveNameShort = mutableMapOf<String, String>(
        "turnLeft" to "l",  "turnRight" to "r", "moveForward" to "w",
        "moveBackward" to "s" , "alarm" to "h"
    )
    protected var support: IssWsHttpKotlinSupport

    init {
        support = IssWsHttpKotlinSupport.getConnectionWs(scope, "${wenvAddr}:8091")
        support.registerActor(this)
        //println( "$name AbstractRobotActor | init $support ${infoThreads()}")
    }

    //val afterConnect : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =  fun(scope, support ) {... }
        //------------------------------------------------

    protected  fun doForward() {
        support.forward(ApplMsgs.forwardMsg)
    }

    protected  fun microStep() {
        support.forward(ApplMsgs.microStepMsg)
        //delay(moveInterval) //to avoid too-rapid movement
    }

    protected  fun doBackward() {
        support.forward(ApplMsgs.backwardMsg)
        //delay(moveInterval) //to avoid too-rapid movement
    }

    protected  fun microBackStep() {
        support.forward(ApplMsgs.littleBackwardMsg)
        //delay(moveInterval) //to avoid too-rapid movement
    }

    protected  fun turnLeft() {
        support.forward(ApplMsgs.turnLeftMsg)
        //delay(moveInterval) //to avoid too-rapid movement
    }

    protected  fun turnRight() {
        support.forward(ApplMsgs.turnRightMsg)
        //delay(moveInterval) //to avoid too-rapid movement
    }

    protected  fun doMove(moveStep: Char) {
        colorPrint( "$name AbstractRobotActor | doMove $moveStep", Color.BLUE)
            if (moveStep == 'w') doForward()
            else if (moveStep == 'l') turnLeft()
            else if (moveStep == 'r') turnRight()
            else if (moveStep == 's') doBackward()
    }

    protected val currentTime: Long
        protected get() = System.currentTimeMillis()

    protected fun getDuration(start: Long): Long {
        return System.currentTimeMillis() - start
    }

    //StartTime = getCurrentTime()  Duration = getDuration(StartTime)
    //protected fun reactivate(actor: IJavaActor) { actor.send(ApplMsgs.resumeMsg) }

    /*
======================================================================================
 */
    //Transform an ApplMessage into a JSONObject by using the HORRIBLE trick
    override suspend fun handleInput(msg: ApplMessage) {
        //println(  "$name | AbstractRobotActor handleInput msg=$msg")
        msgDriven( JSONObject( msg.msgContent ) ) //.replace("@",",") HORRIBLE trick
    }

    protected abstract fun msgDriven(infoJson: JSONObject)


}