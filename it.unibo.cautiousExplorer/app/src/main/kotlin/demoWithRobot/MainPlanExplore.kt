package demoWithRobot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.supports.NaiveActorKotlinObserver
import itunibo.planner.plannerUtil
import kotlinx.coroutines.runBlocking

//import mapRoomKotlin.plannerUtil

object mainPlanRobotDemo {

    fun explore( robot: ActorBasicKotlin ) {
         try {
            plannerUtil.startTimer()

            plannerUtil.initAI()
            println("===== initial map")
            plannerUtil.showMap()


                       //plannerUtil.cell0DirtyForHome()
            plannerUtil.setGoal(1, 1);
            val actions = plannerUtil.doPlan()
             val pathTodo = actions.toString()
             val cmdStr  = ApplMsgs.executorstartMsg
                 .replace("PATHTODO", pathTodo)
                 .replace(",","@")
             println("")
             val cmd = MsgUtil.buildDispatch("main", ApplMsgs.executorStartId, cmdStr, "any")
             robot.send(cmd)
             plannerUtil.showMap()

            plannerUtil.getDuration()

		} catch (e: Exception) {
            e.printStackTrace()
        }

    }

 



}


fun main() {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        //CONFIGURE THE SYSTEM
        val obs     = NaiveActorKotlinObserver("obs", 0, this)
        val myrobot = RobotExecutor("myrobot", obs)
        mainPlanRobotDemo.explore(myrobot)
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}