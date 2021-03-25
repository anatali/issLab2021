package it.unibo.supports
import it.unibo.interaction.MsgRobotUtil
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
object WebSocketKotlinSupportUsage {

    //ENTRY after connection
    val workToDo : (CoroutineScope, WebSocketKotlinSupport) -> Unit =  fun(scope, support ){
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

}

@ExperimentalCoroutinesApi
fun main() = runBlocking {
    println("==============================================")
    println("WebSocketUtilUsage | main start n_Threads=" + Thread.activeCount());
    println("==============================================")
    val support = WebSocketKotlinSupport(this )
    //val ws      =
    support.connect( "localhost:8091", WebSocketKotlinSupportUsage.workToDo)

    println("==============================================")
    println("TestSupportJar | main BEFORE END n_Threads=" + Thread.activeCount());
    println("==============================================")

    //give time to see messages ...
    delay(5000)  //CREATE new threads  !!!
    support.disconnect()

    println("==============================================")
    println("WebSocketUtilUsage | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}
