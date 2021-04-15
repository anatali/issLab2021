/*
============================================================
AbstractRobotActor

============================================================
*/
package it.unibo.robotService

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import it.unibo.supports.IssWsHttpKotlinSupport
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
abstract class AbstractRobotActor(name: String, val wenvAddr: String,
                                  //scope: CoroutineScope= GlobalScope)
         scope: CoroutineScope= CoroutineScope( newSingleThreadContext("single_$name") ))
                                  : ActorBasicKotlin(name, scope, DispatchType.single) {
    protected var moveInterval = 500L //to avoid too-rapid movement
    protected var cnsl = System.console() //returns null in an online IDE
    protected val MoveNameShort: MutableMap<String, String> = HashMap()
    protected lateinit var support: IssWsHttpKotlinSupport

    init {
        support = IssWsHttpKotlinSupport.getConnectionWs(scope, "${wenvAddr}:8091")
        support.registerActor(this)
        MoveNameShort["moveForward"] = "w"
        MoveNameShort["moveBackward"] = "s"
        MoveNameShort["turnLeft"] = "l"
        MoveNameShort["turnRight"] = "r"
        MoveNameShort["alarm"] = "h"
        println( "$name AbstractRobotActor | init $support ${infoThreads()}")
        //doForward()
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
        println( "$name AbstractRobotActor | doMove $moveStep")
        //scope.launch {
            if (moveStep == 'w') doForward()
            else if (moveStep == 'l') turnLeft()
            else if (moveStep == 'r') turnRight()
            else if (moveStep == 's') doBackward()
        //}
    }

    protected val currentTime: Long
        protected get() = System.currentTimeMillis()

    protected fun getDuration(start: Long): Long {
        return System.currentTimeMillis() - start
    }

    //StartTime = getCurrentTime()  Duration = getDuration(StartTime)
    //protected fun reactivate(actor: IJavaActor) { actor.send(ApplMsgs.resumeMsg) }

    fun waitUser(prompt: String) {
        print(">>>  $prompt >>>  ")
        val scanner = Scanner(System.`in`)
        try {
            scanner.nextInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
======================================================================================
 */
    //Transform an ApplMessage into a JSONObject by using the HORRIBLE trick
    override suspend fun handleInput(msg: ApplMessage) {
        //println(  "$name | AbstractRobotActor handleInput msg=$msg")
        msgDriven(JSONObject( msg.msgContent.replace("@",",") ) ) //HORRIBLE trick
    }

    protected abstract fun msgDriven(infoJson: JSONObject)


}