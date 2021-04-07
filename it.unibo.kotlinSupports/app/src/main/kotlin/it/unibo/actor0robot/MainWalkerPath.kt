/*
============================================================
MainWalkerPath

============================================================
 */
package it.unibo.actor0robot

import it.unibo.actor0.*
import kotlinx.coroutines.*


    fun demoDoPath(scope: CoroutineScope){
        val pathTodo   = "wwww"
        val walkerName = "walkerkpath"
        val startmsg   = MsgUtil.buildDispatch("main", "start", pathTodo, walkerName )
        val walker     = WalkerPath(walkerName, scope )
        walker.send(startmsg)
    }


    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainWalkerPath | START ${sysUtil.aboutSystem("MainExecutors")}");
        println("==============================================")

        runBlocking {
            demoDoPath( this )
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainWalkerPath | END TIME=$endTime ${sysUtil.aboutThreads("MainExecutors") }"  );
        println("==============================================")

    }

