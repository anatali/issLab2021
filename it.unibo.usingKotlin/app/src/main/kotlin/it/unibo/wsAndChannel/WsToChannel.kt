/**
 * WebSocketViaChannel
 * @author AN - DISI - Unibo
===============================================================
Use a channel to send commands via a WebSocket (support=okhttp3)
and to receive the answers sent form the WebSocket
===============================================================
 */

package it.unibo.wsAndChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import okhttp3.*        //A non-blocking interface to a web socket


class WsToChannel(val scope : CoroutineScope)  {
    lateinit var ws : WebSocket
    val socketEventChannel: Channel<String> = Channel(10) //our channel buffer is 10 events

    val observer    = WebSocketObserver(scope, socketEventChannel)
    var receiveGoon = true

    fun connect( wsAddr: String ) {
        val request0 = Request.Builder()
                .url("ws://$wsAddr")
                .build()
        val okHttpClient = OkHttpClient()
        ws = okHttpClient.newWebSocket(request0, observer)
    }

    fun disconnect( ) {
        println("disconnecting from $ws")
        var gracefulShutdown = ws.close(1000, "appl_terminated")
    }

    fun getChannel() : Channel<String>{
        return socketEventChannel;
    }

    suspend fun startReceiver(scope : CoroutineScope) {
        val receiver = scope.launch {
            while ( receiveGoon ) {
                val v = socketEventChannel.receive()  //Blocking
                println("RECEIVER | receives $v ")  //in ${curThread()}
            }
        }
        //socketEventChannel.close()
    }//startReceiver

    fun stopReceiver(){
        receiveGoon = false;
    }

}

fun main() = runBlocking {
    println("==============================================")
    println("WsToChannel | main start n_Threads=" + Thread.activeCount());
    println("==============================================")
    val support  = WsToChannel(this)
    support.connect("localHost:8091")

    support.startReceiver(this)

    //give time to see messages ...
    delay(5000)  //CREATE new threads  !!!

    support.stopReceiver()

    println("==============================================")
    println("WsToChannel | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}



