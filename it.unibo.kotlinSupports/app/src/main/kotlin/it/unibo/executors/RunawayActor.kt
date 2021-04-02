package it.unibo.executors

 
import it.unibo.executors.ApplMsgs.endMoveId
import it.unibo.executors.ApplMsgs.runawyEndMsg
import it.unibo.executors.ApplMsgs.runawyStartId
import it.unibo.interaction.IJavaActor
import kotlinx.coroutines.CoroutineScope
import mapRoomKotlin.mapUtil
import org.json.JSONObject

/*
The map is a singleton object, managed by mapUtil
 */
class RunawayActor(name: String, ownerActor: IJavaActor, scope: CoroutineScope) : ExecutorActor(name, ownerActor, scope) {

    /*
    override fun updateTripInfo(move: String) {
        moves.updateMovesRep(move)
        mapUtil.doMove(move)
    }*/
    /*
    override fun resetStateVars() {
        curState = State.start
        moves.cleanMovesRepresentation()
        todoPath = ""
    }*/
    /*
    override fun doMove(moveStep: Char) {
        println("RunawayActor | doMove ... $moveStep totPath=$todoPath")
        if (moveStep == 'w') doStep() else if (moveStep == 'l') turnLeft() else if (moveStep == 'r') turnRight() else if (moveStep == 's') doBackStep()
    }*/

    protected fun fsmrunaway(move: String, endmove: String) {
        println(
            name + " | state=" +
                    curState + " move=" + move + " endmove=" + endmove + " totPath=" + todoPath
        )
        when (curState) {
            State.start -> {
                if (move == runawyStartId) {
                    println("=&=&=&=&=RunawayActor runaway&=&=&=&=&=&=&=&=&=&=& ")
                }
                if (todoPath.length > 0) {
                    mapUtil.showMap()
                    //waitUser();
                    doMove(todoPath.get(0))
                    curState = State.moving
                } else curState = State.endok
            }
            State.moving -> {
                val moveShort = MoveNameShort[move]
                if (endmove == "true") {
                    updateTripInfo(moveShort!!)
                    mapUtil.showMap()
                    todoPath = todoPath.substring(1) //the first move has been done
                    println("$name | moveShort $moveShort totPath=$todoPath")
                    if (todoPath.length > 0) {
                        curState = State.moving
                        doMove(todoPath.get(0))
                    } else { //totPath.length() == 0
                        microStep()
                        curState = State.endok
                    }
                } else {  //endmove=false (obstacle)
                    println("$name|  FATAL ERROR: OUT OF HYPOTHESIS")
                    support.removeActor(this)
                    terminate()
                }
            } //moving
            State.endok -> {
                println("$name | END OK ---------------- ")
                ownerActor.send(runawyEndMsg)
                support.removeActor(this)
                terminate()
            } //end
            else -> {
                println("$name | error - curState = $curState")
            }
        }
    }

    /*
======================================================================================
 */
    override fun msgDriven(msgJson: JSONObject) {
        if (msgJson.has(runawyStartId)) {
            println("RunawayActor | runaway infoJson:$msgJson")
            todoPath = msgJson.getString(runawyStartId)
            fsmrunaway(runawyStartId, "")
        } else if (msgJson.has(endMoveId)) {
            println("RunawayActor | infoJson:$msgJson")
            fsmrunaway(msgJson.getString("move"), msgJson.getString("endmove"))
        }
    }
}