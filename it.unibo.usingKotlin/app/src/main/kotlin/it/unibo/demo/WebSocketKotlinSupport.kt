/**
 * WebSocketKotlinSupport
 * @author AN - DISI - Unibo
===============================================================
A kotlin object that provides utility operations to
connect/disconnect with HTTP and a websocket by using the
library okhttp3

It provides also operations to sendHttp and sendOnWs
===============================================================
 */
package it.unibo.demo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

@ExperimentalCoroutinesApi
class WebSocketKotlinSupport(val scope: CoroutineScope) : WebSocketListener() {
    lateinit var myWs: WebSocket
    lateinit var workTodo: (CoroutineScope, WebSocketKotlinSupport) -> Unit
    //lateinit var connetctedWs : WebSocket

    val  JSON_MediaType = "application/json; charset=utf-8".toMediaType()
    val okHttpClient    = OkHttpClient()

    //After open => execute workTodo that has been set by connect
    override fun onOpen(webSocket: WebSocket, response: Response ) {
        println("WebSocketKotlinSupport | onOpen $response")
        workTodo( scope, this )
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        println("WebSocketKotlinSupport | onMessage $text")
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    }

    override fun onFailure(webSocket:WebSocket, t:Throwable, response:Response?) {
    }

//===============================================================================
    fun connect( wsAddr : String , callback : (CoroutineScope, WebSocketKotlinSupport)->Unit ) : WebSocket {
        workTodo = callback
        val request0 = Request.Builder()
            .url("ws://$wsAddr")
            .build()
        myWs = okHttpClient.newWebSocket(request0, this)
        return myWs
    }

    fun disconnect( ws : WebSocket){ //WebSocketKotlinSupport.disconnect( myWs )
        //see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/close/
        println("WebSocketKotlinSupport | disconnecting from $ws")
        var gracefulShutdown = ws.close(1000, "appl_terminated")
        println("WebSocketKotlinSupport | gracefulShutdown = $gracefulShutdown")
        //gracefulShutdown = ws.close(1000, "appl_terminated")
        //println("WebSocketKotlinSupport |  gracefulShutdown = $gracefulShutdown")

    }
//-----------------------------------------------------------------

    fun sendHttp(msgJson : String, httpaddr : String){
        //val client = OkHttpClient()
        val body    = msgJson.toRequestBody(JSON_MediaType)
        println("WebSocketKotlinSupport | sendHttp msgJson=$msgJson ")
        val request = Request.Builder()
            .url( "http://$httpaddr" )
            .post(body)
            .build()
        val response = okHttpClient.newCall(request).execute()
        println("WebSocketKotlinSupport | post response=$response ")
    }

    //fun sendWs( msgJson : String, ws : WebSocket  ){ ws.send(msgJson); }
    fun sendWs( msgJson : String   ){
        myWs.send(msgJson);
    }

}//WebSocketKotlinSupport


