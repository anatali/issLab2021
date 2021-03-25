/**
 * WebSocketKotlinSupport
 * @author AN - DISI - Unibo
===============================================================
A kotlin object that provides utility operations to
connect/disconnect with HTTP and a websocket by using the
library okhttp3

It provides also operations to sendHttp and sendOnWs

Observer of the socket, it is observable in its turn
===============================================================
 */
package it.unibo.supports
import it.unibo.interaction.IssCommActorSupport
import it.unibo.interaction.IssObserver
import it.unibo.supports2021.ActorBasicJava
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http.RealResponseBody
import org.json.JSONObject
import java.util.*

@ExperimentalCoroutinesApi
class WebSocketKotlinSupport(val scope: CoroutineScope) : WebSocketListener(), IssCommActorSupport {
    lateinit var myWs: WebSocket
    lateinit var workTodo: (CoroutineScope, WebSocketKotlinSupport) -> Unit
    //lateinit var connetctedWs : WebSocket

    val  JSON_MediaType = "application/json; charset=utf-8".toMediaType()
    val okHttpClient    = OkHttpClient()
    //val observers       = Vector<IssObserver>()
    val actorobservers  = Vector<ActorBasicJava>()
    private var opened          = false

    //val socketMsgChannel: Channel<String> = Channel(10) //our channel buffer is 10 events

    //fun getInputChannel() : Channel<String> { return socketMsgChannel }
//===============================================================================
    //After open => execute workTodo that has been set by connect
    override fun onOpen(webSocket: WebSocket, response: Response ) {
        println("WebSocketKotlinSupport | onOpen $response")
        opened = true
        workTodo( scope, this )
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        //wwprintln("WebSocketKotlinSupport | onMessage $text")
        updateObservers( text )
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    }

    override fun onFailure(webSocket:WebSocket, t:Throwable, response:Response?) {
    }
//===============================================================================
    fun updateObservers( msg: String){
        //println("WebSocketKotlinSupport | updateObservers " + observers.size() );
        //observers.forEach{ it.handleInfo(msg) } //loose control
        actorobservers.forEach{ it.send(msg) }
        //scope.launch { socketMsgChannel.send( msg ) }
    }
    //override fun registerObserver(obs: IssObserver) { observers.add(obs) }
    //override fun removeObserver(obs: IssObserver)   { observers.remove(obs) }
    override fun registerActor(obs: ActorBasicJava) { actorobservers.add(obs) }
    override fun removeActor(obs: ActorBasicJava)   { actorobservers.remove(obs); }
    override fun isOpen() : Boolean {  return opened   }
    override fun close(){  disconnect( )  }
//===============================================================================
    override fun forward( msg: String)  {
        sendWs(msg)
    }
    override fun request( msg: String)  {
        sendWs(msg)
    }
    override fun requestSynch( msg: String) : String {
        return "todo"
    }
    override fun reply( msg: String)  {

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

    fun disconnect( ){ //WebSocketKotlinSupport.disconnect( myWs )
        //see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/close/
        println("WebSocketKotlinSupport | disconnecting from $myWs")
        var gracefulShutdown = myWs.close(1000, "appl_terminated")
        println("WebSocketKotlinSupport | gracefulShutdown = $gracefulShutdown")
    }
//-----------------------------------------------------------------

    fun sendHttp(msgJson : String, httpaddr : String) : String {
        //val client = OkHttpClient()
        val body    = msgJson.toRequestBody(JSON_MediaType)
        //println("WebSocketKotlinSupport | sendHttp msgJson=$msgJson ")
        val request = Request.Builder()
            .url( "http://$httpaddr" )
            .post(body)
            .build()
        val response = okHttpClient.newCall(request).execute()
        //println("WebSocketKotlinSupport | post response=$response ")
        val answer  =  response.body!!.string()
        //println("WebSocketKotlinSupport | post answer=$answer ")
        return answer
    }

      fun sendWs( msgJson : String   ){
          //println("sendWs $msgJson")
        myWs.send(msgJson);
    }

}//WebSocketKotlinSupport


