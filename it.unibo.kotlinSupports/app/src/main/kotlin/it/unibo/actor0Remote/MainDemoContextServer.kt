package it.unibo.actor0Remote

import it.unibo.actor0.*
import it.unibo.actor0Usage.ActorBasicKotlinEcho
import kotlinx.coroutines.*


    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainDemoContextServer | START ${sysUtil.aboutSystem("mainCtxServer")}");
        println("==============================================")

        runBlocking {
            //Create a ActorContextTcpServer
            val ctxserver = ActorContextTcpServer("ctxServer", this, Protocol.TCP )

            ActorBasicKotlinEcho("echo", this)

            val caller = RemoteActorEchoCaller("caller", this)
            //Actiate the caller
            caller.send(MsgUtil.startDefaultMsg)
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainDemoContextServer | END TIME=$endTime ${sysUtil.aboutThreads("mainCtxServer") }"  );
        println("==============================================")

    }

