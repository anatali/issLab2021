package it.unibo.wsAndChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import okhttp3.*

@ExperimentalCoroutinesApi
class WebSocketObserver( val scope: CoroutineScope,
                         val socketEventChannel:Channel<String> ) : WebSocketListener() {
lateinit var ws : WebSocket



    /* =============================================================== */

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocketObserver | onOpen $response")
    }
    override fun onMessage(webSocket: WebSocket, msg: String) {
        //println("WebSocketObserver | onMessage $msg")
        scope.launch {
            socketEventChannel.send( msg )
        }
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    }

    override fun onFailure(webSocket:WebSocket, t:Throwable, response:Response?) {
    }
}


