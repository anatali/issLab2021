/*
RobotExecutor.kt

An unibo-actor that executes a given sequence of moves (todoPath)
and returns to its ownerActor
 */
package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.MsgUtil
import it.unibo.robotService.AbstractRobotActor
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import mapRoomKotlin.TripInfo
import org.json.JSONObject
import java.lang.Exception

/*
The map is a singleton object, managed by mapUtil
 */
class RobotExecutor (name: String, protected var ownerActor: ActorBasicKotlin)
                                            : AbstractRobotActor(name, "localhost") {
    protected enum class State {
        start, continueJob, moving, turning, endok, endfail
    }

    protected var curState = State.start
    //protected var moves = RobotMovesInfo(false)
    protected var todoPath = ""
    protected var stepMsg = "{\"ID\":\"350\" }".replace("ID", ApplMsgs.stepId)
    protected var stepper: ActorBasicKotlin? = null
    protected var moves  = TripInfo()



    protected fun resetStateVars() {
        curState = State.start
        //moves.cleanMovesRepresentation()
        todoPath = ""
    }

    protected fun nextMove() {
        if (todoPath.length > 0) {
            moves.showMap()
            val firstMove = todoPath[0]
            //waitUser("nextMove=$firstMove")
            todoPath = todoPath.substring(1)
            if (firstMove == 'w') {
                //support.removeActor(this) //avoid to receive info form WEnv
                val applMsg = MsgUtil.buildDispatch(name,"step",stepMsg,"stepper")
                stepper!!.send(applMsg)
                curState = State.moving
            } else if (firstMove == 'l' || firstMove == 'r')  {
                support.registerActor(this) //TODO
                curState = State.turning
                doMove(firstMove)
            }
        } else { //odoPath.length() == 0
            microStep()
            curState = State.endok
        }
    }

    protected fun obstacleFound() {
        println("$name | END KO ---------------- ")
        moves.showMap()
        try {
            moves.setObstacle()
        } catch (e: Exception) { //wall
            println("$name | outside the map " + e.message)
        }
        moves.showMap()
        ownerActor.send(
            ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.getJourney())
        )
        support.removeActor(this)
        //terminate()
        //resetStateVars();
    }

    protected fun fsm(move: String, endmove: String) {
        println("$name | state=" +
                    curState + " move=" + move + " endmove=" + endmove + " todoPath=" + todoPath
        )
        when (curState) {
            State.start -> {
                if (move == ApplMsgs.executorStartId) {
                    stepper = BasicStepRobotActor("stepper", this, scope, "localhost")
                }
                support.registerActor(this)
                nextMove()
            }
            State.continueJob -> {
                if (move == ApplMsgs.executorStartId) {
                    support.registerActor(this)
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
                support.registerActor(this)
                if (move == ApplMsgs.stepDoneId) {
                //if (move == "moveForward") {
                    moves.updateMovesRep("w")
                    nextMove()
                } else if (move == ApplMsgs.stepFailId) {
                    obstacleFound()
                }
            }
            State.endok -> {
                println("$name | END OK ---------------- ")
                ownerActor.send(ApplMsgs.executorOkEnd(name))
                moves.showMap()
                support.removeActor(this)
                curState = State.continueJob
            } //endok
            State.endfail -> {
                println("$name | END KO ---------------- ")
                try {
                    moves.setObstacle()
                } catch (e: Exception) { //wall
                    println("$name | outside the map " + e.message)
                }
                moves.showMap()
                ownerActor.send(
                    ApplMsgs.executorFailEnd(name, moves.getJourney())
                    //ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.movesRepresentation)
                )
                support.removeActor(this)
                curState = State.continueJob
                //terminate()
            }
            else -> {
                println("$name | error - curState = $curState")
            }
        }
    }

    /*
======================================================================================
 */
    override fun msgDriven(msgJson: JSONObject) {
        if (msgJson.has(ApplMsgs.executorStartId)) {
            println("$name | msgDriven  $msgJson")
            todoPath = msgJson.getString(ApplMsgs.executorStartId)
            fsm(ApplMsgs.executorStartId, "")
        } else if (msgJson.has(ApplMsgs.endMoveId)) {
            //println("$name | msgDriven endMoveId:$msgJson")
            val moveDone = msgJson.getString("move")
            if( moveDone == "moveForward")  return  //IGNORE, since we look at stepDone or stepFail
            else fsm(moveDone, msgJson.getString(ApplMsgs.endMoveId))
        } else if (msgJson.has(ApplMsgs.stepDoneId)) {
            println("$name | msgDriven stepDoneId:$msgJson")
            fsm(ApplMsgs.stepDoneId, "")
        } else if (msgJson.has(ApplMsgs.stepFailId)) {
            println("$name | msgDriven stepFailed:$msgJson")
            fsm(ApplMsgs.stepFailId, msgJson.getString(ApplMsgs.stepFailId))
        }
    }
}