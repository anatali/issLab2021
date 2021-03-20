/**
 * WebSocket8091Observer
 * @author AN - DISI - Unibo
=========================================================================
A kotlin class that defines a WebSocketListener
The main connects/disconnects to WENv socket 8091 by using WebSocket8091ObserverUtil
and the library https://square.github.io/okhttp/
=========================================================================
 */
package it.unibo.demo
import it.unibo.interaction.MsgRobotUtil
import kotlinx.coroutines.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


@ExperimentalCoroutinesApi
class WebSocket8091Observer( val scope: CoroutineScope  ) : WebSocketListener() {
lateinit var myWs     : WebSocket
lateinit var workTodo : (CoroutineScope,WebSocket)->Unit

    override fun onOpen(webSocket: WebSocket, response: Response ) {
        println("WebSocketObserver | onOpen $response")
        myWs = webSocket
        workTodo( scope, webSocket )
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        println("WebSocketObserver | onMessage $text")
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    }

    override fun onFailure(webSocket:WebSocket, t:Throwable, response:Response?) {
    }

    fun connect( wsAddr : String , callback : (CoroutineScope, WebSocket)->Unit ) {
        workTodo = callback
        WebSocket8091ObserverUtil.connectTows(this,wsAddr)
    }

    fun disconnect(){
        WebSocket8091ObserverUtil.disconnect( myWs )
    }


}
//ENTRY after connection
val workToDo : ( CoroutineScope, WebSocket) -> Unit =  fun( scope, ws ){
    //sendWithDelay( ws, MsgRobotUtil.turnLeftMsg,300 )
    scope.launch {
        WebSocket8091ObserverUtil.sendOnWs( MsgRobotUtil.turnLeftMsg, ws )
        delay(300)
        WebSocket8091ObserverUtil.sendOnWs( MsgRobotUtil.turnRightMsg, ws )
        delay(300)
        WebSocket8091ObserverUtil.sendOnWs( MsgRobotUtil.forwardMsg, ws )
        delay(400)
        WebSocket8091ObserverUtil.sendOnWs( MsgRobotUtil.backwardMsg, ws )
        delay(400)
    }
}



fun main() = runBlocking {
    println("==============================================")
    println("WebSocketObserver | main start n_Threads=" + Thread.activeCount());
    println("==============================================")
    val observer  = WebSocket8091Observer(this )
    observer.connect( "localhost:8091",  workToDo  )
    //val ws8091    = WebSocket8091ObserverUtil.connectTows(observer,"localhost:8091")
/*
    WebSocket8091ObserverUtil.sendHttp(MsgRobotUtil.forwardMsg ,  "localhost:8090/api/move")
    delay(1000)
    WebSocket8091ObserverUtil.sendHttp(MsgRobotUtil.forwardMsg , "localhost:8090/api/move")
*/

    //give time to see messages ...
    delay(5000)  //CREATE new threads  !!!
    observer.disconnect()
    //WebSocket8091ObserverUtil.disconnect( ws8091 )
    println("==============================================")
    println("WebSocketObserver | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}