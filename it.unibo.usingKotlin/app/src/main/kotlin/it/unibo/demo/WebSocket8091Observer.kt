package it.unibo.demo
import kotlinx.coroutines.*
import okhttp3.*

@ExperimentalCoroutinesApi
class WebSocket8091Observer(val scope: CoroutineScope) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocketObserver | onOpen $response")
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        println("WebSocketObserver | onMessage $text")
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    }

    override fun onFailure(webSocket:WebSocket, t:Throwable, response:Response?) {
    }
}

object WebSocketObserverUtil {

    fun connectTows8091(observer: WebSocket8091Observer): WebSocket {
        val request0 = Request.Builder()
                .url("ws://localhost:8091")
                .build()
        val okHttpClient = OkHttpClient()
        val ws8091 = okHttpClient.newWebSocket(request0, observer)
        return ws8091
    }

    fun disconnect(ws: WebSocket) {
        //see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/close/
        println("disconnecting from $ws")
        var gracefulShutdown = ws.close(1000, "appl_terminated")
        println("gracefulShutdown = $gracefulShutdown")
        gracefulShutdown = ws.close(1000, "appl_terminated")
        println("gracefulShutdown = $gracefulShutdown")
    }
}
fun main() = runBlocking {
    println("==============================================")
    println("WebSocketObserver | main start n_Threads=" + Thread.activeCount());
    println("==============================================")
    val observer  = WebSocket8091Observer(this)
    val ws8091    = WebSocketObserverUtil.connectTows8091(observer)
    //give time to see messages ...
    delay(5000)  //CREATE new threads  !!!
    WebSocketObserverUtil.disconnect( ws8091 )
    println("==============================================")
    println("WebSocketObserver | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}