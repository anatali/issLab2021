package it.unibo.actor0robot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0robot.ApplMsgs.endMoveId
import it.unibo.actor0robot.ApplMsgs.runawyEndMsg
import it.unibo.actor0robot.ApplMsgs.runawyStartId
import kotlinx.coroutines.CoroutineScope
import mapRoomKotlin.mapUtil
import org.json.JSONObject

/*
The map is a singleton object, managed by mapUtil
 */
class RunawayActor(name: String, ownerActor: ActorBasicKotlin, scope: CoroutineScope)
    : ExecutorActor(name, ownerActor, scope) {


    protected fun fsmrunaway(move: String, endmove: String) {
        println(
            name + " | state=" +
                    curState + " move=" + move + " endmove=" + endmove + " totPath=" + todoPath
        )
        when (curState) {
            State.start -> {
                if (move == runawyStartId) {
                    println("------------ RunawayActor --------------- ")
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