/*
============================================================
MainRobotService

============================================================
 */
package it.unibo.robotService

import it.unibo.actor0.*
import it.unibo.actor0Service.ActorContextTcpServer
import kotlinx.coroutines.*


    fun main() {
        runBlocking {
            //sysUtil.trace = true
            val startBasicRobotMsg = ApplMessage(
                "startbasicrobot",
                "dispatch", "main", "basicRobot",
                "{\"start\":\"dobasic\"}"
            )
            println("==============================================")
            println("MainRobotService | START ${sysUtil.aboutSystem("mainCtxServer")}");
            println("==============================================")

            val ctxserver = ActorContextTcpServer(
                "ctxServer", Protocol.TCP,
                CoroutineScope(newFixedThreadPoolContext(5, "ctxpool5"))
            )

            //NOTE THAT WE USE NAMED ARGS
            BasicStepRobotActor("stepRobot", ctxserver, wenvAddr="localhost", scope=this)
            //BasicStepRobotActor("stepRobot", ctxserver,  scope=this)
        }

    }
