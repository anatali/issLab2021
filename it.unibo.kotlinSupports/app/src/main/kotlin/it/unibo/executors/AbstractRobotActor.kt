package it.unibo.executors

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.supports.IssWsHttpKotlinSupport
import it.unibo.supports.WebSocketKotlinSupportUsage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
abstract class AbstractRobotActor(name: String, scope: CoroutineScope) : ActorBasicKotlin(name, scope) {
    protected var moveInterval = 500L //to avoid too-rapid movement
    protected var support: IssWsHttpKotlinSupport
    protected var cnsl = System.console() //returns null in an online IDE
    protected val MoveNameShort: MutableMap<String, String> = HashMap()

    init {
        support = IssWsHttpKotlinSupport.createForWs(scope, "localhost:8091")
        support.registerActor(this)
        support.wsconnect(  fun(scope, support ) {println("$name | connected ")} )
        MoveNameShort["moveForward"] = "w"
        MoveNameShort["moveBackward"] = "s"
        MoveNameShort["turnLeft"] = "l"
        MoveNameShort["turnRight"] = "r"
        MoveNameShort["alarm"] = "h"
        //println( "$name AbstractRobotActor | init === $support")
    }

    //val afterConnect : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =  fun(scope, support ) {... }
        //------------------------------------------------

    protected  fun doStep() {
        //scope.launch {
            support.forward(ApplMsgs.forwardMsg)
            //delay(moveInterval) //to avoid too-rapid movement
        //}
    }

    protected  fun microStep() {
        support.forward(ApplMsgs.microStepMsg)
        //delay(moveInterval) //to avoid too-rapid movement
    }

    protected  fun doBackStep() {
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
        if (moveStep == 'w') doStep()
        else if (moveStep == 'l') turnLeft()
        else if (moveStep == 'r') turnRight()
        else if (moveStep == 's') doBackStep()
    }

    protected val currentTime: Long
        protected get() = System.currentTimeMillis()

    protected fun getDuration(start: Long): Long {
        return System.currentTimeMillis() - start
    }

    //StartTime = getCurrentTime()  Duration = getDuration(StartTime)
    //protected fun reactivate(actor: IJavaActor) { actor.send(ApplMsgs.resumeMsg) }

    protected fun waitUser(prompt: String) {
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