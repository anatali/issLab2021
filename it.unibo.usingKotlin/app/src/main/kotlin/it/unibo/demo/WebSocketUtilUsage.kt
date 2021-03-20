package it.unibo.demo

import it.unibo.interaction.MsgRobotUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.WebSocket

object WebSocketUtilUsage {
    //ENTRY after connection
    val workToDo : (CoroutineScope, WebSocketKotlinSupport) -> Unit =  fun(scope, support ){
        //sendWithDelay( ws, MsgRobotUtil.turnLeftMsg,300 )
        scope.launch {
            support.sendWs( MsgRobotUtil.turnLeftMsg  )
            delay(300)
            support.sendWs( MsgRobotUtil.turnRightMsg  )
            delay(300)
            support.sendWs( MsgRobotUtil.forwardMsg  )
            delay(400)
            support.sendWs( MsgRobotUtil.backwardMsg  )
            delay(400)
            support.sendHttp(MsgRobotUtil.forwardMsg ,  "localhost:8090/api/move")
            //delay(1000)
            support.sendHttp(MsgRobotUtil.backwardMsg , "localhost:8090/api/move")
        }
    }

}

fun main() = runBlocking {
    println("==============================================")
    println("WebSocketUtilUsage | main start n_Threads=" + Thread.activeCount());
    println("==============================================")
    val support  = WebSocketKotlinSupport(this )
    val ws       = support.connect( "localhost:8091",  WebSocketUtilUsage.workToDo  )
    //val ws8091    = WebSocket8091ObserverUtil.connectTows(observer,"localhost:8091")



    //give time to see messages ...
    delay(5000)  //CREATE new threads  !!!
    support.disconnect(ws)

    println("==============================================")
    println("WebSocketUtilUsage | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}
