/*
============================================================
MainRobotService

============================================================
 */
package it.unibo.robotService

import it.unibo.actor0.*
import kotlinx.coroutines.*


    fun main() {
        runBlocking {
            //sysUtil.trace = true
            val startBasicRobotMsg = ApplMessage(
                MsgUtil.startDefaultId,
                "dispatch", "main", "basicRobot", "{\"start\":\"do\"}"
            )

            println("==============================================")
            println("MainStepRobotService | START ${sysUtil.aboutSystem("mainCtxServer")}");
            println("==============================================")

            val ctxserver = ActorContextTcpServer(
                "ctxServer", Protocol.TCP,
                CoroutineScope(newFixedThreadPoolContext(10, "ctxpool10"))
            )
            /*
            Accepts   ApplMsgs.stepMsg
            Returns   ApplMsgs.stepDoneMsg or ApplMsgs.stepFailMsg
            WARNING !!!!!!!!  Use wenvAddr=wenv if deploy with dockercompose
             */

            StepRobotActor("stepRobot", ctxserver, this) //, "wenv"
            //StepRobotActor("stepRobot", ctxserver, "localhost")
            //stepper.registerActor(ctxserver)

            //val basicrobot = BasicRobotActor("basicRobot", "localhost")
            val basicrobot = BasicRobotActor("basicRobot" )
            basicrobot.send( startBasicRobotMsg )

        }
    }
