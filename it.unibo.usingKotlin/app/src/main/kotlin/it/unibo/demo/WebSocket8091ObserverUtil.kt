/**
 * WebSocket8091ObserverUtil
 * @author AN - DISI - Unibo
===============================================================
A kotlin object that provides utility operations to
connect/disconnect with a websocket by using the
library okhttp3
===============================================================
 */
package it.unibo.demo
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json





object WebSocket8091ObserverUtil {
val  JSON_MediaType = "application/json; charset=utf-8".toMediaType()
lateinit var connetctedWs : WebSocket



    fun connectTows(observer: WebSocket8091Observer, wsaddr:String): WebSocket {
        val request0 = Request.Builder()
                .url("ws://$wsaddr")
                .build()
        val okHttpClient = OkHttpClient()
        connetctedWs = okHttpClient.newWebSocket(request0, observer)
        return connetctedWs
    }

    fun sendHttp(msgJson : String, httpaddr : String){
        val client = OkHttpClient()
        val body   = msgJson.toRequestBody(JSON_MediaType)
        val request = Request.Builder()
                .url( "http://$httpaddr" )
                .post(body)
                .build()
        val response = client.newCall(request).execute()
        println("WebSocket8091ObserverUtil | post response=$response ")
    }

    fun sendOnWs( msgJson : String, ws : WebSocket  ){
        ws.send(msgJson);
    }

    fun disconnect(ws: WebSocket) {
        //see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/close/
        println("WebSocket8091ObserverUtil | disconnecting from $ws")
        var gracefulShutdown = ws.close(1000, "appl_terminated")
        println("WebSocket8091ObserverUtil | gracefulShutdown = $gracefulShutdown")
        //gracefulShutdown = ws.close(1000, "appl_terminated")
        //println("WebSocket8091ObserverUtil |  gracefulShutdown = $gracefulShutdown")
    }


}
