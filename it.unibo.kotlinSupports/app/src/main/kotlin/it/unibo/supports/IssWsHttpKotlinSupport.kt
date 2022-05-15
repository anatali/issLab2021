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
import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.ApplMessageType
import it.unibo.actor0.sysUtil
import it.unibo.interaction.IUniboActor
import it.unibo.interaction.IssActorObservable
import it.unibo.interaction.IssCommSupport
import it.unibo.interaction.IssOperations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
class IssWsHttpKotlinSupport

    private constructor(val scope: CoroutineScope, val ipaddr:String, val wsconn:Boolean)
          : WebSocketListener(), IssActorObservable, IssCommSupport, IssOperations {
    lateinit var myWs: WebSocket
    lateinit var workTodo: (CoroutineScope, IssWsHttpKotlinSupport) -> Unit

    val  JSON_MediaType        = "application/json; charset=utf-8".toMediaType()
    val okHttpClient           = OkHttpClient()
    val actorobservers         = Vector<IUniboActor>()
    private var opened         = false
    //private var connectForWs   = false

    init{
        colorPrint("IssWsHttpKotlinSupport | init - doing nothing")
        /*
         if( wsconn ) myWs = wsconnect(  fun(scope, support ) {
             colorPrint("IssWsHttpKotlinSupport | wsconnect ... ${sysUtil.aboutThreads("isssupport")}")
         } )
          */
    }
    companion object { //singleton
        //val WEnvAddr = "localhost:8091"
        val activeAconnsHttp = HashMap<String,IssWsHttpKotlinSupport>()
        val activeAconnsWs   = HashMap<String,IssWsHttpKotlinSupport>()
        var trace            = false
        var connectForWs     = false

        fun colorPrint(msg : String, color : Color = Color.CYAN ){
            if( trace )
            println(Kolor.foreground("      $msg", color ) )
        }

        fun createForHttp(scope: CoroutineScope, addr: String) : IssWsHttpKotlinSupport{
            colorPrint("IssWsHttpKotlinSupport |  createForHttp:  ", Color.BLUE)
            if( ! activeAconnsHttp.containsKey(addr)) {
                activeAconnsHttp.put(addr, IssWsHttpKotlinSupport(scope, addr, false))
            }
            return activeAconnsHttp.get(addr)!!
        }
        fun createForWs(scope: CoroutineScope, addr: String) : IssWsHttpKotlinSupport{
            if( ! activeAconnsWs.containsKey(addr)) {
                val support = IssWsHttpKotlinSupport(scope, addr, true)
                activeAconnsWs.put(addr,support)
                colorPrint("CREATED A NEW IssWsHttpKotlinSupport for $addr ${sysUtil.aboutThreads("isssupport")}")
            }
            connectForWs = true
            return activeAconnsWs.get(addr)!!
        }

        fun getConnectionWs(scope: CoroutineScope, addr: String) : IssWsHttpKotlinSupport{
            if( ! activeAconnsWs.containsKey(addr)) {
                val support = IssWsHttpKotlinSupport(scope, addr, false)
                activeAconnsWs.put(addr,support)
                //support.wsconnect(  fun(scope, support ) {
                colorPrint("IssWsHttpKotlinSupport | connected ... ${sysUtil.aboutThreads("isssupport")}")
                colorPrint("CREATE A NEW IssWsHttpKotlinSupport for $addr ${sysUtil.aboutThreads("isssupport")}")
            }
            return activeAconnsWs.get(addr)!!
        }
    }

    override fun getConnectionWs(scope: CoroutineScope, addr: String): IssWsHttpKotlinSupport {
        return IssWsHttpKotlinSupport.getConnectionWs(scope, addr)
    }
//===============================================================================
    //After open => execute workTodo that has been set by connect
    override fun onOpen(webSocket: WebSocket, response: Response ) {
        //println("IssWsHttpKotlinSupport | onOpen $response")
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
        //println("IssWsHttpKotlinSupport | updateObservers ${actorobservers.size} $msgAppl" )
        actorobservers.forEach{ it.send(msgAppl) }
    }

    fun cvtJsonToAppl(msgJson : String ) : String{
        //val msgJson = msgJson.replace(",","@") //HORRIBLE trick
        //println( "IssWsHttpKotlinSupport | cvtJsonToAppl $msgJson " )
        //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
        val msgAppl = ApplMessage("supportInfo", ApplMessageType.dispatch.toString(),
                    "support", "observer",
                 msgJson  )

        return msgAppl.toString()
    }


    override fun registerActor(obs: IUniboActor) { actorobservers.add(obs) }
    override fun removeActor(obs: IUniboActor)   { actorobservers.remove(obs); }


    override fun isOpen() : Boolean {  return opened   }
    override fun close(){  disconnect( )  }

//===============================================================================
    fun wsconnect( //wsAddr : String ,
    callback: (CoroutineScope, IssWsHttpKotlinSupport) -> Unit) : WebSocket {
        //colorPrint("IssWsHttpKotlinSupport | wsconnect: $ipaddr", Color.RED)
        workTodo = callback
        val request0 = Request.Builder()
            .url("ws://$ipaddr")
            .build()
        myWs = okHttpClient.newWebSocket(request0, this)
        colorPrint("IssWsHttpKotlinSupport | wsconnect myWs: $myWs", Color.BLUE)
        return myWs
    }

    fun disconnect( ){ //IssWsHttpKotlinSupport.disconnect( myWs )
        //see https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/close/
        colorPrint("IssWsHttpKotlinSupport | disconnecting from $myWs")
        var gracefulShutdown = myWs.close(1000, "appl_terminated")
        colorPrint("IssWsHttpKotlinSupport | gracefulShutdown = $gracefulShutdown")

        okHttpClient.dispatcher.executorService.shutdown()
        okHttpClient.connectionPool.evictAll()

        Thread.sleep(1000)

         sysUtil.showAliveThreads()

    }
//-----------------------------------------------------------------

    fun sendHttp(msgJson : String, httpaddr : String) : String {
        try {
            val body = msgJson.toRequestBody(JSON_MediaType)
            //colorPrint("IssWsHttpKotlinSupport | sendHttp msgJson=$msgJson to $httpaddr ")
            val request = Request.Builder()
                .url("http://$httpaddr")
                .post(body)
                .build()
            val response = okHttpClient.newCall(request).execute()
            //val response = client.newCall(request).execute()
            //colorPrint("IssWsHttpKotlinSupport | post response=$response ")
            val answer = response.body!!.string()
            //colorPrint("IssWsHttpKotlinSupport | post answer=$answer ")
            return answer
        }catch(e:Exception){
            println("IssWsHttpKotlinSupport | ERROR $e ")
            return "{\"endmove\":\"connerror\"}"
        }
    }

      fun sendWs( msgJson : String   ){
          //colorPrint("IssWsHttpKotlinSupport | sendWs $msgJson", Color.RED)
        myWs.send(msgJson);
    }
//========================================================================
    override fun forward(msgJson: String) {
        if (connectForWs) myWs.send(msgJson) else colorPrint("SORRY: not connected for ws", Color.RED)
    }

    override fun request(msgJson: String) {
        if (connectForWs) myWs.send(msgJson) else colorPrint("SORRY: not connected for ws",  Color.RED)
    }

    override fun reply(msgJson: String) {
        if (connectForWs) myWs.send(msgJson) else colorPrint("SORRY: not connected for ws",  Color.RED)
    }

    override fun requestSynch(msgJson: String): String {
        TODO("Not yet implemented")
    }



}//IssWsHttpKotlinSupport


