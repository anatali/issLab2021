package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.supports2021.ActorBasicJava
import kotlinx.coroutines.*

class MainActor0Demo0 {
    suspend fun prodCons(scope : CoroutineScope){
    val numOfConsumers = 10
    val consumers      = arrayOfNulls<ActorKotlinConsumer>(numOfConsumers)


    //CREATE AND START THE CONSUMERS
        for( i in 0..consumers.size-1){
            consumers[i] = ActorKotlinConsumer("cons$i", scope)
            MsgUtil.sendMsg("start", "start", consumers[i]!!)
        }

    //CREATE THE PRODUCER connected to the first consumer
        val prod = ActorKotlinProducer("prod", consumers[0]!!, scope)

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
            MsgUtil.sendMsg("end", "ok", consumers[i]!!)
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
            //appl.prodCons(this)
            appl.prodCons(GlobalScope)
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainActor0Demo0 | END TIME=$endTime ${sysUtil.aboutThreads("applmain") }"  );
        println("==============================================")
    }

