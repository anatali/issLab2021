package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.DispatchType
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.supports2021.ActorBasicJava
import it.unibo.supports2021.usageJavaKotlin.MainUsageActorJavaKotlin
import it.unibo.supports2021.usageJavaKotlin.NaiveActorJavaKotlin
import kotlinx.coroutines.*
import java.util.*

class MainActor0Demo0 {
    companion object{
        val appl_dispatchType = DispatchType.single //DispatchType.cpubound
    }

    suspend fun demoActivateActors(){
        val numOfActors = 5
        val a = Vector<ActorKotlinNaive>()

        for (i in 0..numOfActors-1) {
            val aa  = ActorKotlinNaive("a$i", appl_dispatchType)
            val msg = MsgUtil.buildDispatch("main", "start", "ok", "a$i" )
            a.add(aa);
            aa.forward("start","ok",aa)
            //aa.sendToYourself(msg)
        }
        //MsgUtil.sendMsg("start", "start", a0);

        ActorBasicJava.delay(1000)

        for (i in 0..numOfActors-1) {
            val msg = MsgUtil.buildDispatch("main", "end", "ok", "a$i" )
            a.get(i).forward("end","ok",a.get(i))
            //a.get(i).sendToYourself(msg )
        }

    }
    suspend fun prodCons(){
    val numOfConsumers = 40
    val consumers      = arrayOfNulls<ActorKotlinConsumer>(numOfConsumers)


    //CREATE AND START THE CONSUMERS
        for( i in 0..consumers.size-1){
            consumers[i] = ActorKotlinConsumer("cons$i", appl_dispatchType)
            MsgUtil.sendMsg("start", "start", consumers[i]!!)
        }

    //CREATE THE PRODUCER connected to the first consumer
        val prod = ActorKotlinProducer("prod", consumers[0]!!, appl_dispatchType)

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
            appl.demoActivateActors( )
            //appl.prodCons( )
        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainActor0Demo0 | END TIME=$endTime ${sysUtil.aboutThreads("applmain") }"  );
        println("==============================================")
        //nthreads= 6 at minimum
    }

