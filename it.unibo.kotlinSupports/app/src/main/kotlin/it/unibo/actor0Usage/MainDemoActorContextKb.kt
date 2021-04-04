/*
============================================================
DemoActorBasicContextKb.kt
Creates some ActorBasicKotlinEcho that is recorded in ActorBasicContextKb
and then send to each recorded actor an "end" message to terminate it.
A ActorBasicKotlinNaive is registered as an observer to the ActorBasicKotlinEcho
in order to show that the echo propagates the receives messages

The number of threads is 3 for each valure of numOfActors
============================================================
 */
package it.unibo.actor0Usage

import it.unibo.actor0.*
import kotlinx.coroutines.*

class DemoActorBasicContextKb {
    val numOfActors = 1

    suspend fun activateAndEndActors(scope: CoroutineScope){
        println("--------------------------------------------------------")
        println("DemoActorBasicContextKb | activateActors"  );
        println("--------------------------------------------------------")
        val hellomsg = MsgUtil.buildDispatch("main", "hellomsg", "ok", "any" )
        val endmsg   = MsgUtil.buildDispatch("main", "end",       "ok", "any" )

        val observer = ActorBasicKotlinNaive("obser", scope )
        for (i in 0..numOfActors-1) {
            val newactor  = ActorBasicKotlinEcho("echo$i", scope)
            //newactor.registerActor(observer)
            newactor.send(hellomsg)
        }

        delay(500)

        for (i in 0..numOfActors-1) {
             val a = ActorBasicContextKb.getActor("echo$i")
             if( a != null  ) a.send(endmsg)
             //MsgUtil.sendMsg("end","ok",a)  //an utility to send
        }

        //observer.terminate()      //quite rudetermination
        observer.send(endmsg) //more graceful termination

     }



}//ActorBasicWithContext

    @ExperimentalCoroutinesApi
    fun main(){
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainDemoActorBasicContextKb | START ${sysUtil.aboutSystem("applmain") }"  );
        println("==============================================")

        runBlocking {
            val appl = DemoActorBasicContextKb()
            appl.activateAndEndActors( this )
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainDemoActorBasicContextKb | END TIME=$endTime ${sysUtil.aboutThreads("applmain") }"  );
        println("==============================================")
        //nthreads= 6 at minimum
    }

