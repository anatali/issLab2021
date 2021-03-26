package it.unibo.supports
import it.unibo.interaction.IJavaActor
import it.unibo.interaction.MsgRobotUtil
import it.unibo.supports2021.ActorBasicJava
import it.unibo.supports2021.usage.NaiveActorObserver
import it.unibo.supports2021.usage.RobotActorController
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
object WebSocketKotlinSupportUsage {

    //ENTRY after connection
    val workToDo : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =  fun(scope, support ){
        println("WebSocketUtilUsage | workToDo ... ")

        scope.launch {
            support.sendWs( MsgRobotUtil.turnLeftMsg  )
            delay(300)
            support.sendWs( MsgRobotUtil.turnRightMsg  )
            delay(300)
            support.sendWs( MsgRobotUtil.forwardMsg  )
            delay(400)
            support.sendWs( MsgRobotUtil.backwardMsg  )
            delay(1000)
            var answer = support.sendHttp(MsgRobotUtil.forwardMsg ,  "localhost:8090/api/move")
            println("${answer}")
            delay(1000)
            answer = support.sendHttp(MsgRobotUtil.backwardMsg , "localhost:8090/api/move")
            println("${answer}")
        }

    }

    val testObservers : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =  fun(scope, support ) {
        println("WebSocketKotlinSupportUsage | testObservers 1 ")
        while (!support.isOpen()) {  //busy form of waiting
            println("WebSocketKotlinSupportUsage | testObservers support opening ... ")
            ActorBasicJava.delay(100)
        }
        println("WebSocketKotlinSupportUsage | testObservers 2 ")

        val observers = arrayOfNulls<NaiveActorObserver>(5)
        for (i in 0..4) {
            observers[i] = NaiveActorObserver("a$i", i)
            support.registerActor(observers[i]!!)
        }
        println("WebSocketKotlinSupportUsage | testObservers 3 ")
        support.forward(MsgRobotUtil.turnLeftMsg)

        println("WebSocketKotlinSupportUsage | testObservers 4")
        for (i in 0..4) {
            support.removeActor(observers[i]!!)
            observers[i]!!.terminate()
        }

    }

}

@ExperimentalCoroutinesApi
fun main() = runBlocking {
    println("==============================================")
    println("WebSocketUtilUsage | main start ${ActorBasicJava.aboutThreads()}"  );
    println("==============================================")
    val support = IssWsHttpKotlinSupport.createForWs(this, "localhost:8091" )
    //val ws      =
    //support.connect( "localhost:8091", WebSocketKotlinSupportUsage.workToDo)
    //support.wsconnect(  WebSocketKotlinSupportUsage.workToDo)
    support.wsconnect(WebSocketKotlinSupportUsage.testObservers)

    println("==============================================")
    println("TestSupportJar | main BEFORE END ${ActorBasicJava.aboutThreads()}" );
    println("==============================================")

    //give time to see messages ...
    delay(10000)  //CREATE new threads  !!!
    support.close()

    println("==============================================")
    println("WebSocketUtilUsage | main ${ActorBasicJava.aboutThreads()}");
    println("==============================================")

}
