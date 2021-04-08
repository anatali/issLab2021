/*
===================================================================
ExecutorActor.kt
Accepts an executorstartMsg that specifies a sequence of moves.
Its goal is to execute the given sequence of moves and return:
executorendokMsg  in case of success
executorendkoMsg  with the path done in case of failure (obstacle)
===================================================================
 */
package it.unibo.executors


import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.executors.ApplMsgs.endMoveId
import it.unibo.executors.ApplMsgs.executorStartId
import it.unibo.executors.ApplMsgs.executorendkoMsg
import it.unibo.executors.ApplMsgs.stepDoneId
import it.unibo.executors.ApplMsgs.stepFailId
import it.unibo.interaction.IJavaActor
import it.unibo.supports.IssWsHttpKotlinSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import mapRoomKotlin.mapUtil
import org.json.JSONObject
import java.lang.Exception

/*
The map is a singleton object, managed by mapUtil
 */
open class ExecutorActor(name: String, val ownerActor: ActorBasicKotlin, scope: CoroutineScope)
    : AbstractRobotActor(name,scope) {
    protected enum class State {
        start, nextMove, moving, turning, endok, endfail
    }

    protected var curState = State.start
    protected var moves: RobotMovesInfo = RobotMovesInfo(false)
    //protected var ownerActor: IJavaActor
    protected var todoPath = ""
    protected var stepMsg = ApplMsgs.stepMsg.replace("TIME", "350")
    protected lateinit var stepper : StepRobotActor

    init {
        support = IssWsHttpKotlinSupport.getConnectionWs(scope, "localhost:8091")
        //support.wsconnect(  fun(scope, support ) {println("$name | connected ${infoThreads()}")} )
        println( "$name | ExecutorActor init ${infoThreads()}")
    }

    protected fun updateTripInfo(move: String) {
        moves.updateMovesRep(move)
        mapUtil.doMove(move)
    }

    protected fun resetStateVars() {
        curState = State.start
        moves.cleanMovesRepresentation()
        todoPath = ""
    }

    protected  fun nextMove() {
        if (todoPath.length > 0) {
            mapUtil.showMap()
            //waitUser();
            val firstMove = todoPath[0]
            todoPath = todoPath.substring(1)
            if (firstMove == 'w') {
                support.removeActor(this) //avoid to receive info form WEnv
                //val stepper = StepRobotActor("stepper", this, scope )
                val startmsg = MsgUtil.buildDispatch(name, ApplMsgs.stepId, stepMsg, "stepper" )
                stepper.send(startmsg)
                curState = State.moving
            } else { //firstMove == 'l'
                curState = State.turning
                doMove(firstMove)
            }
        } else { //odoPath.length() == 0
            microStep()
            curState = State.endok
        }
    }

    protected fun obstacleFound() {
        println("$name | obstacleFound ")
        mapUtil.showMap()
        try {
            mapUtil.setObstacle()
        } catch (e: Exception) { //wall
            println(name + " | outside the map " + e.message)
        }
        mapUtil.showMap()
        val koMsg     = executorendkoMsg.replace("PATHDONE", moves.movesRepresentation)
        val answerMsg =  MsgUtil.buildDispatch(name, ApplMsgs.executorFailId, koMsg, ownerActor.name)
        ownerActor.send(answerMsg)
        support.removeActor(this)
        terminate()
        //resetStateVars();
    }

    protected  fun fsm(move: String, endmove: String) {
        println(
            name + " | state=" +
                    curState + " move=" + move + " endmove=" + endmove + " todoPath=" + todoPath
        )
        when (curState) {
            State.start -> {
                if (move == executorStartId) {
                    support.registerActor(this)
                    stepper = StepRobotActor("steprobot", this, scope )
                    nextMove()
                }
            }
            State.turning -> {
                println("$name | turning ... endmove= $endmove")
                val moveShort = MoveNameShort[move]
                if (endmove == "true") {
                    updateTripInfo(moveShort!!)
                    mapUtil.showMap()
                    //waitUser("turning");
                    nextMove()
                } else println("$name | FATAL ERROR ")
            } //turning
            State.moving -> {
                println("$name | moving ... $move")
                support.registerActor(this)
                if (move == stepDoneId) {
                    updateTripInfo("w")
                    nextMove()
                } else {
                    obstacleFound()
                }
            }
            State.endok -> {
                println("$name | END OK ---------------- ")
                //ownerActor.send(ApplMsgs.executorendokMsg)
                val okMsg     = ApplMsgs.executorendokMsg
                val answerMsg =  MsgUtil.buildDispatch(name, ApplMsgs.executorDoneId, okMsg, ownerActor.name)
                ownerActor.send(answerMsg)
                mapUtil.showMap()
                support.removeActor(this)
                terminate()
                //curState = State.start
            } //endok
            State.endfail -> {
                println("$name | END KO ---------------- ")
                try {
                    mapUtil.setObstacle()
                } catch (e: Exception) { //wall
                    println(name + " | outside the map " + e.message)
                }
                mapUtil.showMap()
                ownerActor.send(
                    ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.movesRepresentation)
                )
                support.removeActor(this)
                terminate()
                //curState = State.start
            }//endfail
            else -> {
                println("$name | error - curState = $curState")
            }
        }
    }

    /*
======================================================================================
 */
    override fun msgDriven(msgJson: JSONObject) {
        if (msgJson.has(executorStartId)) {
            //println("$name | executorStartId:$msgJson")
            todoPath = msgJson.getString(executorStartId)
            fsm(executorStartId, todoPath)
        } else if (msgJson.has(endMoveId)) {
            //println("$name | endMoveId:$msgJson")
            fsm(msgJson.getString("move"), msgJson.getString(endMoveId))
        } else if (msgJson.has(stepDoneId)) {
            //println("$name | stepDoneId:$msgJson")
            fsm(stepDoneId, "")
        } else if (msgJson.has(stepFailId)) {
            //println("$name | stepFailed:$msgJson")
            fsm(stepFailId, msgJson.getString(stepFailId))
        }
    }


}