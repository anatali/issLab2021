package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.MsgUtil
import mapRoomKotlin.mapUtil.doMove
import mapRoomKotlin.mapUtil.showMap
import mapRoomKotlin.mapUtil.setObstacle
import it.unibo.cautiousExplorer.RobotMovesInfo
import it.unibo.executor.ApplMsgs
import it.unibo.interaction.IJavaActor
import it.unibo.robotService.AbstractRobotActor
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.lang.Exception

/*
The map is a singleton object, managed by mapUtil
 */
class RobotExecutor (name: String, protected var ownerActor: ActorBasicKotlin)
                                            : AbstractRobotActor(name, "localhost") {
    protected enum class State {
        start, nextMove, moving, turning, endok, endfail
    }

    protected var curState = State.start
    protected var moves = RobotMovesInfo(false)
    protected var todoPath = ""
    protected var stepMsg = "{\"ID\":\"350\" }".replace("ID", ApplMsgs.stepId)
    protected var stepper: ActorBasicKotlin? = null


    protected fun updateTripInfo(move: String?) {
        moves.updateMovesRep(move)
        doMove(move!!)
    }

    protected fun resetStateVars() {
        curState = State.start
        moves.cleanMovesRepresentation()
        todoPath = ""
    }

    protected fun nextMove() {
        if (todoPath.length > 0) {
            showMap()
            val firstMove = todoPath[0]
            waitUser("nextMove=$firstMove");
            todoPath = todoPath.substring(1)
            if (firstMove == 'w') {
                support.removeActor(this) //avoid to receive info form WEnv
                //IJavaActor stepper = new StepRobotActor("stepper", this );
                //delay(300) //give time to open ws
                val applMsg = MsgUtil.buildDispatch(name,"step",stepMsg,"stepper")
                stepper!!.send(applMsg)
                curState = State.moving
            } else if (firstMove == 'l' || firstMove == 'r')  {
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
        showMap()
        try {
            setObstacle()
        } catch (e: Exception) { //wall
            println("$name | outside the map " + e.message)
        }
        showMap()
        ownerActor.send(
            ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.movesRepresentation)
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
                    stepper = //StepRobotActor("stepper", this)
                        BasicStepRobotActor(
                            "stepper", this, scope, "localhost")
                }
                nextMove()
            }
            State.turning -> {
                println("$name | turning ... endmove= $endmove")
                val moveShort = MoveNameShort[move]
                if (endmove == "true") {
                    updateTripInfo(moveShort)
                    showMap()
                    //waitUser("turning");
                    nextMove()
                } else println("$name | FATAL ERROR ")
            } //turning
            State.moving -> {
                println("$name | moving ... $move")
                support.registerActor(this)
                if (move == ApplMsgs.stepDoneId) {
                    updateTripInfo("w")
                    nextMove()
                } else {
                    obstacleFound()
                }
            }
            State.endok -> {
                println("$name | END OK ---------------- ")
                ownerActor.send(HabitualMsgs.executorOkEnd(name))
                showMap()
                support.removeActor(this)
                //terminate()
            } //end
            State.endfail -> {
                println("$name | END KO ---------------- ")
                try {
                    setObstacle()
                } catch (e: Exception) { //wall
                    println("$name | outside the map " + e.message)
                }
                showMap()
                ownerActor.send(
                     HabitualMsgs.executorFailEnd(name, moves.movesRepresentation)
                    //ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.movesRepresentation)
                )
                support.removeActor(this)
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
            println("$name |  $msgJson")
            todoPath = msgJson.getString(ApplMsgs.executorStartId)
            fsm(ApplMsgs.executorStartId, "")
        } else if (msgJson.has(ApplMsgs.endMoveId)) {
            println("$name | endMoveId:$msgJson")
            fsm(msgJson.getString("move"), msgJson.getString(ApplMsgs.endMoveId))
        } else if (msgJson.has(ApplMsgs.stepDoneId)) {
            println("$name | stepDoneId:$msgJson")
            fsm(ApplMsgs.stepDoneId, "")
        } else if (msgJson.has(ApplMsgs.stepFailId)) {
            println("$name | stepFailed:$msgJson")
            fsm(ApplMsgs.stepFailId, msgJson.getString(ApplMsgs.stepFailId))
        }
    }
}