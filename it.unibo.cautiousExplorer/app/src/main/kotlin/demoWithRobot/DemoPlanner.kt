package demoWithRobot

import aima.core.agent.Action
import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import itunibo.planner.plannerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.json.JSONObject


class DemoPlanner( name: String, scope: CoroutineScope) : ActorBasicKotlin(name,scope) {

    //private lateinit var executor: PathExecutor
    var counter = 0;
    var myexecutor = PathExecutor("executor${counter++}", scope, this)

    fun initWork(){
        plannerUtil.initAI()
        plannerUtil.showMap()
        //executor = PathExecutor("executor", scope, this)
    }

    fun getPlanTodo( actions: List<Action>?) : String{
        val pathTodo = actions.toString()
            .replace("[","").replace("]","")
            .replace(", ","")
        colorPrint("$name | THE PLANNER PROPOSES THE PATH $pathTodo", Color.LIGHT_RED)
        return pathTodo
    }

    fun doThePath(pathTodo: String){
        //myexecutor.terminate()
        //myexecutor  = PathExecutor("executor${counter++}", scope, this)
        this.waitUser("exec")
        val cmdStr= ApplMsgs.executorstartMsg.replace("PATHTODO", pathTodo)
        val cmd   = MsgUtil.buildDispatch(name, ApplMsgs.executorStartId,cmdStr,"executor")
        myexecutor.send(cmd)
    }

    fun planWithEmptyMap() {
        plannerUtil.setGoal(3, 3);
        val actions  = plannerUtil.doPlan()
        val pathTodo = getPlanTodo(actions)
        doThePath(pathTodo)
        plannerUtil.showMap()
     }

    fun planWithNOTEmptyMap(x: Int, y: Int) {
        //executor.terminate()
        plannerUtil.setGoal(x, y);
        val actions  = plannerUtil.doPlan()
        val pathTodo = getPlanTodo(actions)
        doThePath(pathTodo)
    }


    fun handleEndPathExecution(resultJson : String){
        val resJson = JSONObject( resultJson )
        if( resJson.has("executorFail")){
            this.waitUser("backToHome")
            planWithNOTEmptyMap(0,0)
        }else{
            this.waitUser("goto(3,3)")
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
