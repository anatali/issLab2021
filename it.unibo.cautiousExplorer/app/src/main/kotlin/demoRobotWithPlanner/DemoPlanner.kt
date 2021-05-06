/*
DemoPlanner
shows the behavior of a planner that moves a robot using PathExecutor
*/
package demoRobotWithPlanner

import aima.core.agent.Action
import com.andreapivetta.kolor.Color
import demoWithRobot.NaiveObserver
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import itunibo.planner.plannerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.json.JSONObject


class DemoPlanner( name: String, scope: CoroutineScope) : ActorBasicKotlin(name,scope) {
    var counter = 0;
    var answerMove = Channel<String>()
    var answerPath = Channel<String>()
    val obsRobot = MarchegianiObserverForSendingAnswer("obsanswer",  scope,answerMove )
    val robot    = BasicStepRobotActor("stepRobot",obsRobot, scope, "localhost")
    val obs1     = MarchegianiObserverForSendingAnswer("obspath",  scope, answerPath )
    var myexecutor = PathExecutor("executor${counter++}", scope, robot, obs1)



    fun initWork(){
        plannerUtil.initAI()
        plannerUtil.showMap()
        obsRobot.owner = myexecutor
    }

    fun getPlanTodo( actions: List<Action>?) : String{
        val pathTodo = actions.toString()
            .replace("[","").replace("]","")
            .replace(", ","")
        colorPrint("$name | THE PLANNER PROPOSES THE PATH $pathTodo", Color.LIGHT_RED)
        return pathTodo
    }

    suspend fun doThePath(pathTodo: String){
        this.waitUser("exec")
        val cmdStr= ApplMsgs.executorstartMsg.replace("PATHTODO", pathTodo)
        val cmd   = MsgUtil.buildDispatch(name, ApplMsgs.executorStartId,cmdStr,"executor")
        myexecutor.send(cmd)
        colorPrint("$name | wait on channel ... ", Color.BLUE)
        val answer = answerPath.receive()
    }

    suspend fun planWithEmptyMap() {
        plannerUtil.setGoal(3, 3);
        val actions  = plannerUtil.doPlan()
        val pathTodo = getPlanTodo(actions)
        doThePath(pathTodo)
        plannerUtil.showMap()
     }

    suspend fun planWithNOTEmptyMap(x: Int, y: Int) {
        //executor.terminate()
        plannerUtil.setGoal(x, y);
        val actions  = plannerUtil.doPlan()
        val pathTodo = getPlanTodo(actions)
        doThePath(pathTodo)
    }


    suspend fun handleEndPathExecution(resultJson : String){
        val resJson = JSONObject( resultJson )
        if( resJson.has("executorFail")){
            waitUser("backToHome")
            planWithNOTEmptyMap(0,0)
        }else{
            waitUser("goto(3,3)")
            planWithNOTEmptyMap(3,3)  //attempt to reach again
        }
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput $msg")
        if (msg.msgId == "start") {
            initWork()
            planWithEmptyMap()
        }else if (msg.msgId == "executorend") {
            plannerUtil.showMap()
            handleEndPathExecution(msg.msgContent)
        }
    }
}
fun main() {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val demop = DemoPlanner("demop", this )
        //BasicStepRobotActor("stepRobot", ownerActor=demop, this, "localhost")
        demop.send( ApplMsgs.startAny("main") )
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
