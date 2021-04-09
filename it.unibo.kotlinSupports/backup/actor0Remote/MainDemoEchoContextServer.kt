/*
============================================================
MainDemoEchoContextServer

============================================================
 */

package it.unibo.actor0Remote
import it.unibo.actor0.*
import it.unibo.actor0Usage.ActorBasicKotlinEcho
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainDemoEchoContextServer | START ${sysUtil.aboutSystem("mainCtxServer")}");
        println("==============================================")

        runBlocking {
            //Create a ActorContextTcpServer
            val ctxserver = ActorContextTcpServer("ctxServer", Protocol.TCP, this ) //this
            val echoActor = ActorBasicKotlinEcho("echo", this)
            echoActor.registerActor(ctxserver)
        }//runBlocking

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainDemoEchoContextServer | END TIME=$endTime ${sysUtil.aboutThreads("mainCtxServer")}");
        println("==============================================")

    }

