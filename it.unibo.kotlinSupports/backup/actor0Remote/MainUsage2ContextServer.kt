package it.unibo.actor0Remote

import it.unibo.actor0.*
import it.unibo.actor0Usage.ActorBasicKotlinEcho
import kotlinx.coroutines.*


    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        println("==============================================")
        println("MainUsage2ContextServer | START ${sysUtil.aboutSystem("mainCtxServer")}");
        println("==============================================")
        val startTime = sysUtil.aboutSystem("applmain")
        runBlocking {
             val caller2 = RemoteActorEchoCaller("caller2", this, DispatchType.single )
             caller2.send(MsgUtil.startDefaultMsg)
        }//runBlocking

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainUsage2ContextServer | END TIME=$endTime ${sysUtil.aboutThreads("mainusage")}");
        println("==============================================")

    }

