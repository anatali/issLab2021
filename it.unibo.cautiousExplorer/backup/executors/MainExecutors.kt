package it.unibo.executors

import it.unibo.actor0.*
import kotlinx.coroutines.*

    fun demoStepper(scope: CoroutineScope){
        val stepperName = "stepper"
        val startmsg = MsgUtil.buildDispatch("main", "start", "ok", stepperName )
        val stepper = Stepper(stepperName, scope)
        stepper.send(startmsg)
    }
    fun demoDoPath(scope: CoroutineScope){
        val pathTodo   = "ww"
        val walkerName = "walker"
        val startmsg   = MsgUtil.buildDispatch("main", "start", pathTodo, walkerName )
        val walker     = Walker(walkerName, scope )
        walker.send(startmsg)
    }


    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainExecutors | START ${sysUtil.aboutSystem("MainExecutors")}");
        println("==============================================")

        runBlocking {
            //demoStepper( this )
            demoDoPath( this )
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainExecutors | END TIME=$endTime ${sysUtil.aboutThreads("MainExecutors") }"  );
        println("==============================================")

    }

