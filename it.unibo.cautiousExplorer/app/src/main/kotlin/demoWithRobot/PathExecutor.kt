/*
PathExecutor.kt

An unibo-actor that executes a given sequence of moves (todoPath)
and returns to its ownerActor
 */
package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import mapRoomKotlin.TripInfo
import org.json.JSONObject
import java.lang.Exception

/*
The map is a singleton object, managed by mapUtil
 */
@Suppress("REDUNDANT_ELSE_IN_WHEN")
class PathExecutor (name: String, scope: CoroutineScope, protected var ownerActor: ActorBasicKotlin)
    : ActorBasicKotlin( name, scope ) {
                                            //: AbstractRobotActor(name, "localhost") {
    protected enum class State { start, continueJob, moving, turning }

    protected var stepper = BasicStepRobotActor("stepper", this, scope, "localhost")

    protected val MoveNameShort = mutableMapOf<String, String>(
        "turnLeft" to "l",  "turnRight" to "r", "moveForward" to "w",
        "moveBackward" to "s" , "alarm" to "h"
    )

    protected var curState = State.start
    protected var todoPath = ""
    protected var moves  = TripInfo()

    protected fun resetStateVars() {
        curState = State.start
        //moves.cleanMovesRepresentation()
        todoPath = ""
    }

    protected fun nextMove()   { //return true when nothing to to
        if( todoPath.length == 0 ){
             endOk()
        } else {
            moves.showMap()
            val firstMove = todoPath[0]
            //waitUser("nextMove=$firstMove")
            todoPath = todoPath.substring(1)
            if (firstMove == 'w') {
                stepper.send( ApplMsgs.stepRobot_step("$name", "350") )
                curState = State.moving
            } else if (firstMove == 'l' || firstMove == 'r')  {
                curState = State.turning
                stepper.send( ApplMsgs.stepRobot_turn("$name", ""+firstMove) )
            }
        }

    }

    protected fun obstacleFound() {
        println("$name | END FAIL ---------------- ")
        //moves.showMap()
        try {
            moves.setObstacle()
        } catch (e: Exception) { //wall
            println("$name | outside the map " + e.message)
        }
        moves.showMap()

        ownerActor.send(ApplMsgs.executorFailEnd(name, moves.getJourney())
        )
    }

    fun endOk(){
        println("$name | END OK ---------------- ")
        ownerActor.send(ApplMsgs.executorOkEnd(name))
        moves.showMap()
        curState = State.continueJob
    }

    protected fun fsm(move: String, endmove: String) {
        println("$name | state=" +
                    curState + " move=" + move + " endmove=" + endmove + " todoPath=" + todoPath
        )
        when (curState) {
            State.start -> {
                nextMove()
            }
            State.continueJob -> {
                if (move == ApplMsgs.executorStartId) {
                    nextMove()
                }
            }
            State.turning -> {
                println("$name | turning ... $move endmove= $endmove")
                val moveShort = MoveNameShort[move]
                if (endmove == "true") {
                    moves.updateMovesRep(moveShort!!)
                    moves.showMap()
                    nextMove()
                } else println("$name | FATAL ERROR ")
            } //turning
            State.moving -> {
                println("$name | moving ... $move")
                if (move == ApplMsgs.stepDoneId) {
                     moves.updateMovesRep("w")
                    nextMove()
                } else if (move == ApplMsgs.stepFailId) {
                    obstacleFound()
                }
            }
        }
    }

    /*
======================================================================================
 */
    override suspend fun handleInput(msg: ApplMessage) {
        val msgJson = JSONObject( msg.msgContent )
        if (msgJson.has(ApplMsgs.executorStartId)) {
            println("$name | handleInput  $msgJson")
            todoPath = msgJson.getString(ApplMsgs.executorStartId)
            fsm(ApplMsgs.executorStartId, "")
        } else if (msgJson.has(ApplMsgs.endMoveId)) {
            //println("$name | handleInput endMoveId:$msgJson")
            val moveDone = msgJson.getString("move")
            fsm(moveDone, msgJson.getString(ApplMsgs.endMoveId))
        } else if (msgJson.has(ApplMsgs.stepDoneId)) {
            println("$name | handleInput stepDoneId:$msgJson")
            fsm(ApplMsgs.stepDoneId, "")
        } else if (msgJson.has(ApplMsgs.stepFailId)) {
            println("$name | handleInput stepFailed:$msgJson")
            fsm(ApplMsgs.stepFailId, msgJson.getString(ApplMsgs.stepFailId))
        }
    }


}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val path     = "wlwwwwwwrwrr" //wlwwwwwwrwrr
        val cmdStr   = ApplMsgs.executorstartMsg.replace("PATHTODO", path)
        val cmd      = MsgUtil.buildDispatch("main",ApplMsgs.executorStartId,cmdStr,"executor")
        println("main | $cmd")
        val obs      = NaiveObserverActorKotlin("peobs",  this)
        val executor = PathExecutor("pathExec", this, obs)
        executor.send(cmd)
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
