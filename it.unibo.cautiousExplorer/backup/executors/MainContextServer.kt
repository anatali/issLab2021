package it.unibo.executors

import it.unibo.actor0.*
import kotlinx.coroutines.*




    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainContextServer | START ${sysUtil.aboutSystem("mainCtxServer")}");
        println("==============================================")

        runBlocking {
            //ActorContextServer("ctxServer", this, Protocol.TCP )
            //val obs    = NaiveObserver("obs", this )
            //Walker( "walker", this)

            val caller = NaiveCaller("caller", this)
            //delay(1000)
            caller.send(MsgUtil.startDefaultMsg)

        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainContextServer | END TIME=$endTime ${sysUtil.aboutThreads("mainCtxServer") }"  );
        println("==============================================")

    }

