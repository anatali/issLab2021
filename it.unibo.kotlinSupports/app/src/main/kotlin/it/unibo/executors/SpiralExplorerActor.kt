package it.unibo.executors
import mapRoomKotlin.mapUtil.showMap
import mapRoomKotlin.mapUtil.doMove
import it.unibo.interaction.IJavaActor
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class SpiralExplorerActor(name: String, scope: CoroutineScope) : AbstractRobotActor(name, scope) {
    private var numSpiral = 0
    private var engaged = false
    var curPathTodo = ""
    
    private fun getSpiralPath(n: Int): String {
        var path = ""
        var ahead = ""
        for (i in 1..n) {
            ahead = ahead + "w"
        }
        path = ahead + "l" + ahead + "l" + ahead + "l" + ahead + "l"
        return path
    }

    protected fun startNewJourney() {
        numSpiral++
        println("$name | startNewJourney numSpiral=$numSpiral")
        if (numSpiral >= 5) {
            println("$name | TERMINATES")
            terminate()
            return
        }
        curPathTodo = getSpiralPath(numSpiral)
        val msg = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo)
        println("$name | msg=$msg numSpiral=$numSpiral")
        val executor: IJavaActor = ExecutorActor("executor", this, scope )
        waitUser("start new journey")
        executor.send(msg)
    }

    protected fun returnToDen(result: String) {
        val pathStillTodo = curPathTodo.replaceFirst(result.toRegex(), "")
        println("$name | pathStillTodo=$pathStillTodo over:$curPathTodo")
        val pathTodo = reverse(result).replace("l", "r") + "ll" //
        val msg = ApplMsgs.runawyStartMsg.replace("PATHTODO", pathTodo)
        println("$name | msg=$msg numSpiral=$numSpiral")
        turn180()
        //if( pathTodo.startsWith("l") )
        showMap()
        waitUser("return to den")
        support.removeActor(this)
        val runaway: IJavaActor = RunawayActor("runaway", this, scope )
        runaway.send(msg)
    }

    protected fun turn180() {
        turnLeft()
        doMove("l")
        turnLeft()
        doMove("l")
    }

    protected fun reverse(s: String): String {
        return if (s.length <= 1) s else reverse(s.substring(1)) + s[0]
    }

    /*
-----------------------------------------------------------------------------
 */
    override fun msgDriven(mJson: JSONObject) {
        if (mJson.has(ApplMsgs.activateId) && !engaged) {  //while working no more
            engaged = true //if engaged, the message is lost. We could store it in a local queue
            startNewJourney()
        } else
            if( mJson.has(ApplMsgs.executorFailId) ) {
                returnToDen( mJson.getString(ApplMsgs.executorFailId) )
            }else if( mJson.has(ApplMsgs.executorDoneId) ){ //all ok
                startNewJourney()
            }
            /*
            if (mJson.has(ApplMsgs.executorEndId)) {
            val result = mJson.getString(ApplMsgs.executorEndId)
            println("$name | result of journey=$result")
            if (result == "ok") { //Executor has done a spiral
                startNewJourney()
            } else { //Executor has found an obstacle
                returnToDen(result)
            }*/
        else if (mJson.has(ApplMsgs.runawyEndId)) {
            support.registerActor(this)
            startNewJourney()
        }
    } //msgdriven
}