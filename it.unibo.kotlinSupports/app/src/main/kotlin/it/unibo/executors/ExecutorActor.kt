package it.unibo.executors


import it.unibo.executors.ApplMsgs.endMoveId
import it.unibo.executors.ApplMsgs.executorStartId
import it.unibo.executors.ApplMsgs.stepDoneId
import it.unibo.executors.ApplMsgs.stepFailId
import it.unibo.interaction.IJavaActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import mapRoomKotlin.mapUtil
import org.json.JSONObject
import java.lang.Exception

/*
The map is a singleton object, managed by mapUtil
 */
open class ExecutorActor(name: String, ownerActor: IJavaActor, scope: CoroutineScope) : AbstractRobotActor(name,scope) {
    protected enum class State {
        start, nextMove, moving, turning, endok, endfail
    }

    protected var curState = State.start
    protected var moves: RobotMovesInfo = RobotMovesInfo(false)
    protected var ownerActor: IJavaActor
    protected var todoPath = ""
    protected var stepMsg = "{\"ID\":\"350\" }".replace("ID", ApplMsgs.stepId)
    
    protected fun updateTripInfo(move: String) {
        moves.updateMovesRep(move)
        mapUtil.doMove(move)
    }

    protected fun resetStateVars() {
        curState = State.start
        moves.cleanMovesRepresentation()
        todoPath = ""
    }
/*
    protected  fun doMove(moveStep: Char) {
        println("$name | doMove ... $moveStep todoPath=$todoPath")
        if (moveStep == 'w') doStep()
        else if (moveStep == 'l') turnLeft()
        else if (moveStep == 'r') turnRight()
        else if (moveStep == 's') doBackStep()
    }
*/
    protected  fun nextMove() {
        if (todoPath.length > 0) {
            mapUtil.showMap()
            //waitUser();
            val firstMove = todoPath[0]
            todoPath = todoPath.substring(1)
            if (firstMove == 'w') {
                support.removeActor(this) //avoid to receive info form WEnv
                val stepper: IJavaActor = StepRobotActor("stepper", this, scope )
                //delay(300L) //give time to open ws
                stepper.send(stepMsg)
                curState = State.moving
            } else { //firstMove == 'l'
                curState = State.turning
                doMove(firstMove)
            }
        } else { //odoPath.length() == 0
            //microStep()
            curState = State.endok
        }
    }

    protected fun obstacleFound() {
        println("$name | END KO ---------------- ")
        mapUtil.showMap()
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
                    println("=&=&=&=&=ExecutorActor&=&=&=&=&=&=&=&=&=&=& ")
                }
                nextMove()
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
                ownerActor.send(ApplMsgs.executorendokMsg)
                mapUtil.showMap()
                support.removeActor(this)
                terminate()
            } //end
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
        if (msgJson.has(executorStartId)) {
            println("$name | executorStartId:$msgJson")
            todoPath = msgJson.getString(executorStartId)
            fsm(executorStartId, "")
        } else if (msgJson.has(endMoveId)) {
            println("$name | endMoveId:$msgJson")
            fsm(msgJson.getString("move"), msgJson.getString(endMoveId))
        } else if (msgJson.has(stepDoneId)) {
            println("$name | stepDoneId:$msgJson")
            fsm(stepDoneId, "")
        } else if (msgJson.has(stepFailId)) {
            println("$name | stepFailed:$msgJson")
            fsm(stepFailId, msgJson.getString(stepFailId))
        }
    }

    init {
        this.ownerActor = ownerActor
    }
}