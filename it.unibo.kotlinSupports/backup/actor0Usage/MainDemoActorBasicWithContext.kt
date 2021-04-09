package it.unibo.actor0Usage

import it.unibo.actor0.*
import kotlinx.coroutines.*

class ActorBasicWithContext {
    val numOfActors = 1

    suspend fun activateActorsWithContext(scope: CoroutineScope){
        println("--------------------------------------------------------")
        println("ActorBasicWithContext | activateActorsWithContext"  );
        println("--------------------------------------------------------")
        val hellomsg = MsgUtil.buildDispatch("main", "hellomsg", "ok", "any" )
        val endmsg   = MsgUtil.buildDispatch("main", "end", "ok", "any" )

        //val a = Vector<ActorKotlinNaive>()
        //val localctx = ActorContextLocal.getLocalContext(scope)
        for (i in 0..numOfActors-1) {
            val newactor = ActorBasicContextKb.createActor(//ActorContextLocal.createActor(
                    "a$i","ActorBasicKotlinNaive",scope)
            //val startactor = MsgUtil.buildDispatch("main", "startTheActor", "ok", newactor.name )
            newactor.send(hellomsg)
          }
          delay(1000)

        for (i in 0..numOfActors-1) {
             val a = ActorBasicContextKb.getActor("a$i") //ActorContextLocal.getActor("a$i")
             if( a != null  ) //a.sendToYourself(endmsg )
                 //MsgUtil.sendMsg("end","ok",a)
                 MsgUtil.sendMsg("stopTheActor","ok",a)
        }

        //localctx.terminate()
     }



}//ActorBasicWithContext

    @ExperimentalCoroutinesApi
    fun main(){
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainDemoActorBasicWithContext | START ${sysUtil.aboutSystem("applmain") }"  );
        println("==============================================")

        runBlocking {
            val appl = ActorBasicWithContext()
            appl.activateActorsWithContext( this )
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainDemoActorBasicWithContext | END TIME=$endTime ${sysUtil.aboutThreads("applmain") }"  );
        println("==============================================")
        //nthreads= 6 at minimum
    }

