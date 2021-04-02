package it.unibo.executors

import it.unibo.actor0.*
import kotlinx.coroutines.*
import org.json.JSONObject

class MasterActor(name:String, scope: CoroutineScope) : ActorBasicKotlin(name, scope) {
    val stepMsg = ApplMsgs.stepMsg.replace("TIME", "350")

    var stepper      : ActorBasicKotlin
    var startStepMsg : ApplMessage

    init{
        stepper = StepRobotActor("stepper", this, scope)
        startStepMsg =
                MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, stepper.name )
    }

    fun doStep(){
        //MsgUtil.sendMsg("main", ApplMsgs.stepId, ApplMsgs.stepMsg.replace("TIME", "350"), stepper)
        stepper.sendToYourself( startStepMsg )
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name MasterActor | handleInput $msg ${sysUtil.aboutThreads(name)}");
        if( msg.msgId == "start" ) doStep()
        else if( msg.msgId == "stepAnswer" ){
            val answer = JSONObject(msg.msgContent)
            if( answer.has("stepDone")) doStep()
        }
    }


}
class MainExecutors {
      val startmsg = MsgUtil.buildDispatch("main", "start", "ok", "any" )
    /*
      fun demoStepRobotActor(scope: CoroutineScope) {
       scope.launch {
           val obs = NaiveObserver("obs", scope )
           obs.actor.send(startmsg)
           var stepper = StepRobotActor("stepper", obs, scope)
           println("MainExecutors | created observer ${sysUtil.aboutThreads("demo")}");

           for (i in 1..2) {
               println("MainExecutors | created stepper ${sysUtil.aboutThreads("demo")}");
               //delay(1000)  //give the time to connect ...
               //sender : String, msgId: String, msg: String, destActor: ActorBasicKotlin
               val stepMsg = ApplMsgs.stepMsg.replace("TIME", "350")
               val startStepMsg =
                       MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, stepper.name )
               //MsgUtil.sendMsg("main", ApplMsgs.stepId, ApplMsgs.stepMsg.replace("TIME", "350"), stepper)
               stepper.sendToYourself( startStepMsg )
           }
       }
     }*/

}//MainExecutors

    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainExecutors | START ${sysUtil.aboutSystem("MainExecutors")}");
        println("==============================================")

        runBlocking {
           // val appl = MainExecutors()
           // appl.demoStepRobotActor( this )
            val startmsg = MsgUtil.buildDispatch("main", "start", "ok", "any" )
            val ma = MasterActor("ma", this)
            ma.sendToYourself(startmsg)
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainActor0Demo0 | END TIME=$endTime ${sysUtil.aboutThreads("MainExecutors") }"  );
        println("==============================================")

    }

