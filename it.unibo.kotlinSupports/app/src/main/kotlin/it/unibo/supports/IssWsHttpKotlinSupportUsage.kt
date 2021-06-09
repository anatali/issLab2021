package it.unibo.supports
import it.unibo.actor0.sysUtil
import it.unibo.actorAppl.NaiveActorKotlinObserver
import it.unibo.interaction.MsgRobotUtil

import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
object WebSocketKotlinSupportUsage {

    //ENTRY after connection
    val workToDo : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =  fun(scope, support ){
        println("WebSocketUtilUsage | workToDo ... ")

        scope.launch {
            support.sendWs( MsgRobotUtil.turnLeftMsg  )
            delay(400)
            support.sendWs( MsgRobotUtil.turnRightMsg  )
            delay(400)
            support.sendWs( MsgRobotUtil.forwardMsg  )
            delay(500)
            support.sendWs( MsgRobotUtil.backwardMsg  )
            delay(1000)
            var answer = support.sendHttp(MsgRobotUtil.forwardMsg ,  "localhost:8090/api/move")
            println("${answer}")
            delay(1000)
            answer = support.sendHttp(MsgRobotUtil.backwardMsg , "localhost:8090/api/move")
            println("${answer}")
        }

    }

    val testObservers : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =
        fun(scope, support ) {
        println("WebSocketKotlinSupportUsage | testObservers 1 ")
        /*
        while (!support.isOpen()) {  //busy form of waiting
            println("WebSocketKotlinSupportUsage | testObservers support opening ... ")
            ActorBasicJava.delay(100)
        }*/
        println("WebSocketKotlinSupportUsage | testObservers 2 ")

        val observers = arrayOfNulls<NaiveActorKotlinObserver>(5)
        for (i in 1..1) {
            observers[i] = NaiveActorKotlinObserver("observer$i", scope)
            support.registerActor(observers[i]!!)
        }
        //println("WebSocketKotlinSupportUsage | testObservers 3 ")
        //support.forward(MsgRobotUtil.turnLeftMsg)

        //delay(3000)
        //println("WebSocketKotlinSupportUsage | testObservers 4")
            /*
        for (i in 0..1) {
            support.removeActor(observers[i]!!)
            observers[i]!!.terminate()
        }*/

    }

}

suspend fun testWs(scope: CoroutineScope){
    val support = IssWsHttpKotlinSupport.createForWs(scope, "localhost:8091" )
    //val ws      =
    //support.connect( "localhost:8091", WebSocketKotlinSupportUsage.workToDo)
    //support.wsconnect(  WebSocketKotlinSupportUsage.workToDo)
    support.wsconnect(WebSocketKotlinSupportUsage.testObservers)
    support.forward(MsgRobotUtil.turnLeftMsg)
    delay(1000)
    support.forward(MsgRobotUtil.turnRightMsg)
    //give time to see messages ...
    delay(30000)  //CREATE new threads  !!!
    support.close()

}
suspend fun testHttp(scope: CoroutineScope){
    val support = IssWsHttpKotlinSupport.createForHttp(scope , "localhost:8090")
    val answer  = support.sendHttp(MsgRobotUtil.turnLeftMsg,"localhost:8090/api/move")
    println("testHttp answer=$answer")
}
@ExperimentalCoroutinesApi
fun main() = runBlocking {
    println("==============================================")
    println("WebSocketUtilUsage | main start ${sysUtil.aboutThreads("main")}"  );
    println("==============================================")
    testWs(this)
    //testHttp(this)
    println("==============================================")
    println("WebSocketUtilUsage | END ${sysUtil.aboutThreads("main")}");
    println("==============================================")

}
