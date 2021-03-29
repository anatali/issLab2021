/**
 * IssWsHttpKotlinSupport
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
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.ApplMessageType
import it.unibo.interaction.IJavaActor
import it.unibo.interaction.IssActorObservable
import it.unibo.interaction.IssCommSupport
import it.unibo.interaction.IssOperations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

@ExperimentalCoroutinesApi
class IssWsHttpKotlinSupport
    private constructor(val scope: CoroutineScope, val addr:String, val wsconn:Boolean)
     : WebSocketListener(), IssActorObservable, IssCommSupport, IssOperations {
    lateinit var myWs: WebSocket
    lateinit var workTodo: (CoroutineScope, IssWsHttpKotlinSupport) -> Unit

    val  JSON_MediaType = "application/json; charset=utf-8".toMediaType()
    val okHttpClient    = OkHttpClient()
    val actorobservers         = Vector<IJavaActor>()
    private var opened         = false
    private var connectForWs   = true

    //val socketMsgChannel: Channel<String> = Channel(10) //our channel buffer is 10 events
    //fun getInputChannel() : Channel<String> { return socketMsgChannel }

    companion object {
        fun createForHttp(scope: CoroutineScope, addr: String) : IssWsHttpKotlinSupport{
            return IssWsHttpKotlinSupport(scope, addr, false)
        }
        fun createForWs(scope: CoroutineScope, addr: String) : IssWsHttpKotlinSupport{
            return IssWsHttpKotlinSupport(scope, addr, true)
        }
    }
//===============================================================================
    //After open => execute workTodo that has been set by connect
    override fun onOpen(webSocket: WebSocket, response: Response ) {
        println("IssWsHttpKotlinSupport | onOpen $response")
        opened = true
        workTodo( scope, this )
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        //wwprintln("IssWsHttpKotlinSupport | onMessage $text")
        updateObservers( text )
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    }

    override fun onFailure(webSocket:WebSocket, t:Throwable, response:Response?) {
    }
//===============================================================================
    fun updateObservers( msg: String){
        val msgAppl = cvtJsonToAppl(msg)
        println("IssWsHttpKotlinSupport | updateObservers ${actorobservers.size} $msgAppl" )
        actorobservers.forEach{ it.send(msgAppl) }
    }

    fun cvtJsonToAppl(msgJson : String ) : String{
        val msgJson = msgJson.replace(",","@") //HORRIBLE trick
        println( "IssWsHttpKotlinSupport |  $msgJson " )
        //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
        val msgAppl = ApplMessage("supportInfo", ApplMessageType.dispatch.toString(),
                    "support", "observer",
                 msgJson  )
                //"\""+msgJson+"\"" )
        return msgAppl.toString()
    }


    override fun registerActor(obs: IJavaActor) { actorobservers.add(obs) }
    override fun removeActor(obs: IJavaActor)   { actorobservers.remove(obs); }
    override fun isOpen() : Boolean {  return opened   }
    override fun close(){  disconnect( )  }

//===============================================================================
    fun wsconnect( //wsAddr : String ,
    callback: (CoroutineScope, IssWsHttpKotlinSupport) -> Unit
) : WebSocket {
        workTodo = callback
        val request0 = Request.Builder()
            .url("ws://$addr")
            .build()
        myWs = okHttpClient.newWebSocket(request0, this)
        return myWs
    }

    fun disconnect( ){ //IssWsHttpKotlinSupport.disconnect( myWs )
        //see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/close/
        println("IssWsHttpKotlinSupport | disconnecting from $myWs")
        var gracefulShutdown = myWs.close(1000, "appl_terminated")
        println("IssWsHttpKotlinSupport | gracefulShutdown = $gracefulShutdown")
    }
//-----------------------------------------------------------------

    fun sendHttp(msgJson : String, httpaddr : String) : String {
        //val client = OkHttpClient()
        val body    = msgJson.toRequestBody(JSON_MediaType)
        //println("IssWsHttpKotlinSupport | sendHttp msgJson=$msgJson ")
        val request = Request.Builder()
            .url( "http://$httpaddr" )
            .post(body)
            .build()
        val response = okHttpClient.newCall(request).execute()
        //println("IssWsHttpKotlinSupport | post response=$response ")
        val answer  =  response.body!!.string()
        //println("IssWsHttpKotlinSupport | post answer=$answer ")
        return answer
    }

      fun sendWs( msgJson : String   ){
          //println("sendWs $msgJson")
        myWs.send(msgJson);
    }
//========================================================================
    override fun forward(msgJson: String) {
        if (connectForWs) myWs.send(msgJson) else println("SORRY: not connected for ws")
    }

    override fun request(msgJson: String) {
        if (connectForWs) myWs.send(msgJson) else println("SORRY: not connected for ws")
    }

    override fun reply(msgJson: String) {
        if (connectForWs) myWs.send(msgJson) else println("SORRY: not connected for ws")
    }

    override fun requestSynch(msg: String): String {
        TODO("Not yet implemented")
    }

}//IssWsHttpKotlinSupport


