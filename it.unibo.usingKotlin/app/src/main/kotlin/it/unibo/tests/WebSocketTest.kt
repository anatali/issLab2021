package it.unibo.tests

import it.unibo.temp.SocketAbortedException
import it.unibo.temp.SocketUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@ExperimentalCoroutinesApi
class WebSocketTest : WebSocketListener() {

    //val socketEventChannel: Channel<SocketUpdate> = Channel(10)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocketTest | onOpen $response")
        //webSocket.send("Hi")
        //webSocket.send("Hi again")
        //webSocket.send("Hi again again")
        //webSocket.send("Hi again again again")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("WebSocketTest | onMessage $text")
        GlobalScope.launch {
            //socketEventChannel.send(SocketUpdate(text))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        GlobalScope.launch {
            //socketEventChannel.send(SocketUpdate(exception = SocketAbortedException()))
        }
        //webSocket.close(NORMAL_CLOSURE_STATUS, null)
        //socketEventChannel.close()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            //socketEventChannel.send(SocketUpdate(exception = t))
        }
    }
}

fun translate(cmd: String) : String{ //cmd is written in application-language
    var jsonMsg = "{\"robotmove\":\"MOVE\" , \"time\": DURATION}"  //"{ 'type': 'alarm', 'arg': -1 }"
    when( cmd ){
        "msg(w)", "w" -> jsonMsg = jsonMsg.replace("MOVE","moveForward").replace("DURATION", "400")
        "msg(s)", "s" -> jsonMsg = jsonMsg.replace("MOVE","moveBackward").replace("DURATION", "400")
        //"{ 'type': 'moveBackward', 'arg': -1 }"
        "msg(a)", "a", "l" -> jsonMsg = jsonMsg.replace("MOVE","turnLeft").replace("DURATION", "300")
        //"{ 'type': 'turnLeft',  'arg': -1  }"
        "msg(d)", "d", "r" -> jsonMsg = jsonMsg.replace("MOVE","turnRight").replace("DURATION", "300")
        //"{ 'type': 'turnRight', 'arg': -1  }"
        //"msg(l)", "l" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': 300 }"
        //"msg(r)", "r" -> jsonMsg = "{ 'type': 'turnRight', 'arg': 300 }"
        //"msg(z)", "z" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': -1  }"
        //"msg(x)", "x" -> jsonMsg = "{ 'type': 'turnRight', 'arg': -1  }"
        "msg(h)", "h" -> jsonMsg = jsonMsg.replace("MOVE","alarm").replace("DURATION", "100")
        //"{ 'type': 'alarm',     'arg': 100 }"
        else -> println("WEnvConnSupport command $cmd unknown")
    }
    val jsonObject = JSONObject( jsonMsg )
    val msg        =  jsonObject.toString()
    //println("WEnvConnSupport | translate output= $msg ")
    return msg
}

fun main() {
    println("==============================================")
    println("WEnvConnSupport | main start n_Threads=" + Thread.activeCount());
    println("==============================================")

    val JSON = "application/json; charset=utf-8".toMediaType();

    val okHttpClient = OkHttpClient()
    val body = translate("w").toRequestBody(JSON);

    val request0      = Request.Builder()
            .url("ws://localhost:8091" )
            .build()
    /*
    val request      = Request.Builder()
            .url("http://localhost:8090/api/move" )
            .post( body )
            .build()
*/
    okHttpClient.newWebSocket(request0, WebSocketTest() )

    //val response  = okHttpClient.newCall( request).execute()
    //println("WEnvConnSupport | response" + response );

    println("==============================================")
    println("WEnvConnSupport | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}