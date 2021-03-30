package it.unibo.actor0Usage

import it.unibo.actor0.*
import kotlinx.coroutines.*
import java.util.*

class MainActor0Demo0 {
    companion object{
        val appl_dispatchType = DispatchType.single //DispatchType.cpubound
        val numOfActors = 50
    }

    @ExperimentalCoroutinesApi
    suspend fun demoActivateActors(scope: CoroutineScope){
        println("--------------------------------------------------------")
        println("MainActor0Demo0 | demoActivateActors"  );
        println("--------------------------------------------------------")
        val startmsg = MsgUtil.buildDispatch("main", "start", "ok", "any" )
        val endmsg   = MsgUtil.buildDispatch("main", "end", "ok", "any" )
        //val aa = Vector<ActorKotlinNaive>()
        for (i in 0..numOfActors-1) {
            val a = ActorKotlinNaive("a$i", scope,DispatchType.single)
            a.actor.send(startmsg)
            //aa.add(a)
        }
        //for (i in 0..numOfActors-1) { aa.get(i).actor.send(endmsg) }
        for (i in 0..numOfActors-1) {
            val a = ActorContextNaive.getActor("a$i") //ActorContextLocal.getActor("a$i")
            a!!.terminate()
        }
    }

    suspend fun demoActivateActorsWithContext(scope: CoroutineScope){
        println("--------------------------------------------------------")
        println("MainActor0Demo0 | demoActivateActorsWithContext"  );
        println("--------------------------------------------------------")
        val hellomsg = MsgUtil.buildDispatch("main", "hellomsg", "ok", "any" )
        val endmsg   = MsgUtil.buildDispatch("main", "end", "ok", "any" )

        //val a = Vector<ActorKotlinNaive>()
        //val localctx = ActorContextLocal.getLocalContext(scope)
        for (i in 0..numOfActors-1) {
            val newactor = ActorContextNaive.createActor(//ActorContextLocal.createActor(
                    "a$i","it.unibo.actor0Usage.ActorKotlinNaive",scope)
            //val startactor = MsgUtil.buildDispatch("main", "startTheActor", "ok", newactor.name )
            newactor.sendToYourself(hellomsg)
          }

          delay(1000)

        for (i in 0..numOfActors-1) {
             val a = ActorContextNaive.getActor("a$i") //ActorContextLocal.getActor("a$i")
             if( a != null  ) //a.sendToYourself(endmsg )
                 //MsgUtil.sendMsg("end","ok",a)
                 MsgUtil.sendMsg("stopTheActor","ok",a)
        }

        //localctx.terminate()
     }

    suspend fun doNothing(){
        println("--------------------------------------------------------")
        println("MainActor0Demo0 | prodCons"  );
        println("--------------------------------------------------------")

    }
    suspend fun prodCons(scope: CoroutineScope){
        println("--------------------------------------------------------")
        println("MainActor0Demo0 | prodCons"  );
        println("--------------------------------------------------------")
    val numOfConsumers = 10
    val consumers      = arrayOfNulls<ActorKotlinConsumer>(numOfConsumers)


    //CREATE AND START THE CONSUMERS
        for( i in 0..consumers.size-1){
            consumers[i] = ActorKotlinConsumer("cons$i", scope)    //, appl_dispatchType
            MsgUtil.sendMsg("start", "start", consumers[i]!!)
        }

    //CREATE THE PRODUCER connected to the first consumer
        val prod = ActorKotlinProducer("prod", consumers[0]!!, scope) //, appl_dispatchType

    //REGISTER SOME CONSUMER AS PRODUCER OBSERVER
        for( i in 1..consumers.size-1){ //consumers[i] added as an observer
            prod.registerActor( consumers[i]!! )
        }
    //START THE PRODUCER
        //MsgUtil.sendMsg("start", "start", cons)
        MsgUtil.sendMsg("start", "ok", prod)

        delay(500)  //give time to consume

     //TERMINATE THE PRODUCER AND THE CONSUMER OBSERVERS
        MsgUtil.sendMsg("end", "ok", prod)
        for( i in 1..consumers.size-1){ //consumers[0] ended by producer
            val a = ActorContextNaive.getActor("cons$i") //ActorContextLocal.getActor("cons$i")
            if( a != null  ) //a.sendToYourself(endmsg )
                MsgUtil.sendMsg("stopTheActor","ok",a)
            //MsgUtil.sendMsg("end", "ok", consumers[i]!!)
        }
    }
}

    @ExperimentalCoroutinesApi
    fun main(){
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainActor0Demo0 | START ${sysUtil.aboutSystem("applmain") }"  );
        println("==============================================")

        runBlocking {
            val appl = MainActor0Demo0()
            //appl.doNothing()
            appl.demoActivateActors( this )
            //appl.demoActivateActorsWithContext( this )
            //appl.prodCons( this )
            /*
            val startmsg = MsgUtil.buildDispatch("main", "start", "ok", "any" )
            val endmsg   = MsgUtil.buildDispatch("main", "end", "ok", "any" )
            val aa = Vector<ActorKotlinNaive>()
            for (i in 0..MainActor0Demo0.numOfActors-1) {
                val a = ActorKotlinNaive("a$i", this,DispatchType.single)
                a.actor.send(startmsg)
                aa.add(a)
            }
            for (i in 0..MainActor0Demo0.numOfActors-1) {
                aa.get(i).actor.send(endmsg)
            }

             */

        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainActor0Demo0 | END TIME=$endTime ${sysUtil.aboutThreads("applmain") }"  );
        println("==============================================")
        //nthreads= 6 at minimum
    }

