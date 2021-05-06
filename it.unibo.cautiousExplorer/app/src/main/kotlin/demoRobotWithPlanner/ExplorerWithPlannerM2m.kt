/*
ExplorerWithPlannerM2m
Explores a room by executing a sequence of plans (as an oracle)
The proposed plan is executed by the PathExecutor POST API dopath on port 8081
Thus, ExplorerWithPlannerM2m USES in SYNCH way a remote service

 */
package demoRobotWithPlanner

import demoWithRobot.CallRestWithApacheHTTP
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import itunibo.planner.plannerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mapRoomKotlin.mapUtil
import org.json.JSONObject
import java.util.*

class ExplorerWithPlannerM2m( name: String, scope: CoroutineScope) : ActorBasicKotlin(name,scope) {

     private var targetCell = 1

    fun initWork(){
        plannerUtil.initAI()
        println("===== initial map")
        //plannerUtil.showMap()
    }

    fun explore() {
        plannerUtil.setGoal(targetCell, targetCell);
        val actions  = plannerUtil.doPlan()
        val pathTodo = actions.toString()
            .replace("[","").replace("]","")
            .replace(", ","")
        val result = CallRestWithApacheHTTP.doPath(pathTodo)       //request-response
        updateMapAfterPathexecution( result )
     }

    fun updateMapAfterPathexecution(result : String ){
        val resultJson = JSONObject( result )
        if( resultJson.has("executorDone"))  updateMapOk( resultJson.getString("executorDone") )
        else updateMapFail( result )
        continueExploration()
    }

    fun updateMapOk( path: String){
        println("$name | updateMapOk $path")
        val pathSeq = path.toList()
        pathSeq.forEach({ v -> mapUtil.doMove("$v")})
        plannerUtil.showMap()
    }
    fun updateMapFail( path: String){
        println("$name | updateMapFail $path")
        val pathSeq = path.toList()
        pathSeq.forEach({ v -> mapUtil.doMove("$v")})
        mapUtil.setObstacle()
        plannerUtil.showMap()
    }
    fun backToHome(){
        plannerUtil.setGoal(0, 0);
        val actions = plannerUtil.doPlan()
        if( actions== null||  actions.size == 0 ) return
        println("$name | backToHome actions=$actions")
        val pathTodo = actions.toString()
            .replace("[","").replace("]","")
            .replace(", ","") + "l"
        //added a final turnLeft to restore initial state
        val result = CallRestWithApacheHTTP.doPath(pathTodo)
        updateMapAfterPathexecution( result )
    }

    fun continueExploration(){
        val atHome = plannerUtil.atHome()
        println("$name | atHome: ${atHome}")
        if( ! atHome ) {
            //waitUser("backToHome")
            backToHome()
        }
        else{ //the system says that we are at home
            targetCell++
            mywaitUser("explore ($targetCell,$targetCell)")
            if( targetCell <= 4) explore()
            else println("BYE")
        }
    }

    //waitUser inherited does not work
    fun mywaitUser(prompt: String) {
        print(">>>  $prompt >>>  ")
        val scanner = Scanner(System.`in`)
        try {
            scanner.nextInt()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if (msg.msgId == "start") {
            initWork()
            explore()
        }
    }
}
fun main() {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val explorer = ExplorerWithPlannerM2m("explorer", this )
        explorer.send( ApplMsgs.startAny("main") )
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
