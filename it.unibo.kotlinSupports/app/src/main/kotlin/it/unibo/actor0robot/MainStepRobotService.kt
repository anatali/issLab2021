/*
============================================================
MainStepRobotService

============================================================
 */
package it.unibo.actor0robot

import it.unibo.actor0.*
import kotlinx.coroutines.*

    @ExperimentalCoroutinesApi
    fun main() = runBlocking{
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("main")
        println("==============================================")
        println("MainStepRobotService | START ${sysUtil.aboutSystem("mainCtxServer")}");
        println("==============================================")

            val ctxserver = ActorContextTcpServer("ctxServer", Protocol.TCP,
                    CoroutineScope( newFixedThreadPoolContext(10, "ctxpool10") ) )
            /*
            Accepts   ApplMsgs.stepMsg
            Returns   ApplMsgs.stepDoneMsg or ApplMsgs.stepFailMsg
            Use wenvAddr=wenv if deploy with dockercompose
             */
            val stepper = StepRobotActor("stepRobot", ctxserver, "wenv", this)
            //stepper.registerActor(ctxserver)


        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainStepRobotService | END TIME=$endTime ${sysUtil.aboutThreads("main") }"  );
        println("==============================================")

    }

