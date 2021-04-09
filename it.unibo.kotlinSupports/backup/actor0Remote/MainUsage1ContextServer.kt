package it.unibo.actor0Remote

import it.unibo.actor0.*
import it.unibo.actor0Usage.ActorBasicKotlinEcho
import kotlinx.coroutines.*


    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        println("==============================================")
        println("MainUsageContextServer | START ${sysUtil.aboutSystem("mainCtxServer")}");
        println("==============================================")
        val startTime = sysUtil.aboutSystem("applmain")
        runBlocking {
            val caller1 = RemoteActorEchoCaller("caller1", this, DispatchType.single )
            caller1.send(MsgUtil.startDefaultMsg)
        }//runBlocking

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainUsageContextServer | END TIME=$endTime ${sysUtil.aboutThreads("mainusage")}");
        println("==============================================")

    }

