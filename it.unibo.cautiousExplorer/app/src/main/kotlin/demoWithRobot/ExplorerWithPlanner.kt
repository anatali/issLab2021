package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import itunibo.planner.plannerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


class CautiousRobotExplorer( name: String, scope: CoroutineScope) : ActorBasicKotlin(name,scope) {


    private lateinit var executor: PathExecutor

    fun initWork(){
        plannerUtil.initAI()
        println("===== initial map")
        plannerUtil.showMap()
        executor = PathExecutor("executor", scope, this)
    }
    fun explore() {
        plannerUtil.setGoal(1, 1);
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
        if( actions==null ) return
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
            plannerUtil.showMap()
            val atHome = plannerUtil.atHome()
            println("$name | atHome: ${atHome}")
            //plannerUtil.showCurrentRobotState()
            if( ! atHome ) {
                waitUser("backToHome")
                backToHome()
            }
            /*
            else{ //the planner says that we are at home
                if( ! atHomeAgain ) { //turnLeft to restore initial state
                    atHomeAgain = true
                    val cmdStr  = ApplMsgs.executorstartMsg.replace("PATHTODO", "l")
                    val cmd = MsgUtil.buildDispatch("main", ApplMsgs.executorStartId, cmdStr, "executor")
                    executor.send(cmd)
                }
            }*/
        }
    }
}
fun main() {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val cautious = CautiousRobotExplorer("cautious", this )
        cautious.send( ApplMsgs.startAny("main") )
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
