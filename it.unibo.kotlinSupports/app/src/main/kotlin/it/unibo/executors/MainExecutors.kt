package it.unibo.executors

import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.actor0Usage.MainActor0Demo0
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainExecutors {
    suspend fun demoStepRobotActor(scope: CoroutineScope) {
        val obs = NaiveObserver("obs", scope)
        for (i in 1..3){
            var stepper = StepRobotActor("stepper$i", obs, scope)
            //delay(1000)  //give the time to connect ...
            //sender : String, msgId: String, msg: String, destActor: ActorBasicKotlin
            MsgUtil.sendMsg("main", ApplMsgs.stepId, ApplMsgs.stepMsg.replace("TIME", "350"), stepper)
            delay(1000)
        }
     }

}//MainExecutors

    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainExecutors | START ${sysUtil.aboutSystem("MainExecutors")}");
        println("==============================================")

        runBlocking {
            val appl = MainExecutors()
            appl.demoStepRobotActor( this )
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainActor0Demo0 | END TIME=$endTime ${sysUtil.aboutThreads("MainExecutors") }"  );
        println("==============================================")

    }

