package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.supports.NaiveActorKotlinObserver
import itunibo.planner.plannerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class CautiousRobotExplorer( name: String, scope: CoroutineScope) : ActorBasicKotlin(name,scope) {


    private lateinit var executor: RobotExecutor

    fun initWork(){
        plannerUtil.initAI()
        println("===== initial map")
        plannerUtil.showMap()
        executor = RobotExecutor("executor", this)
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

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if (msg.msgId == "start") {
            initWork()
            explore()

        }else if (msg.msgId == "executorend") {
            backToHome()
        }
    }
}
fun main() {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val cautious    = CautiousRobotExplorer("cautious", this )
        cautious.send( HabitualMsgs.startAny("main") )
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
