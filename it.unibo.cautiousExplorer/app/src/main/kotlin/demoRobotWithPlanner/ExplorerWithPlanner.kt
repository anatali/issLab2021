/*
ExplorerWithPlanner
Uses the stepRobot to explore a room by executing a plan
The proposed plan is executed by the PathExecutor component
 */
package demoRobotWithPlanner

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import itunibo.planner.plannerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class ExplorerWithPlanner( name: String, scope: CoroutineScope) : ActorBasicKotlin(name,scope) {

    private lateinit var executor: PathExecutor
    private var targetCell = 1

    fun initWork(){
        plannerUtil.initAI()
        println("===== initial map")
        plannerUtil.showMap()
        executor = PathExecutor("executor", scope, this)
    }
    fun explore() {
        plannerUtil.setGoal(targetCell, targetCell);
        val actions = plannerUtil.doPlan()
        val pathTodo = actions.toString()
            .replace("[","").replace("]","")
            .replace(", ","")
        val cmdStr  = ApplMsgs.executorstartMsg
            .replace("PATHTODO", pathTodo)
        println("$name | cmdStr=$cmdStr")
        val cmd = MsgUtil.buildDispatch("main", ApplMsgs.executorStartId, cmdStr, "executor")
        executor.send(cmd)
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
        val cmdStr  = ApplMsgs.executorstartMsg.replace("PATHTODO", pathTodo)
        println("$name | backToHome cmdStr=$cmdStr")
        val cmd = MsgUtil.buildDispatch("main", ApplMsgs.executorStartId, cmdStr, "executor")
        executor.send(cmd)
        plannerUtil.showMap()
    }

    //var atHomeAgain = false

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if (msg.msgId == "start") {
            initWork()
            explore()
        }else if (msg.msgId == "executorend") {
            //msg(executorend,dispatch,executor,any,{"executorFail":"wwl" },20)
            plannerUtil.showMap()
            val atHome = plannerUtil.atHome()
            println("$name | atHome: ${atHome}")
            //plannerUtil.showCurrentRobotState()
            if( ! atHome ) {
                //waitUser("backToHome")
                    //delay(350)  //to avoid not allowed
                backToHome()
            }
            else{ //the system says that we are at home
                targetCell++
                waitUser("explore ($targetCell,$targetCell)")
                if( targetCell <= 5) explore()
                else println("BYE")
            }
        }
    }
}
fun main() {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val explorer = ExplorerWithPlanner("explorer", this )
        explorer.send( ApplMsgs.startAny("main") )
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
