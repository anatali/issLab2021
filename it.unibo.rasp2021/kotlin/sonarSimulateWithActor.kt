package kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor 
import java.io.BufferedReader
import java.io.InputStreamReader
import javacode.CoapSupport
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
 

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
class sonarSimulateWithActor( val name : String, scope: CoroutineScope = GlobalScope )    {
	var coapSupport : CoapSupport	 

	val actor = scope.actor<ApplMessage>{
 			    for (msg in channel) { // iterate over incoming messages
			        when ( msg.msgId()  ) {
						"start" -> scope.launch{ readInputData() }
			            else -> throw Exception( "unknown" )
			        }
			    }
			}
	

	init{
		coapSupport = CoapSupport( "coap://localhost:5683" )
	}

    suspend fun readInputData(){
        var dataCounter = 1
        while( true ){
 			 var data = readLine()
             println("data ${dataCounter++} = $data " )
             val m = MsgUtil.buildEvent(name, "sonar", "sonar($data)")
             println("EMIT to CoAP: $m"  )
			 if( ! coapSupport.updateResource( m.toString() ) ) println("EMIT failure"  )
        }
    }

}

	
fun main() = runBlocking {
	val a = sonarSimulateWithActor("sonarSimulateWithActor")
	val m = MsgUtil.buildEvent("", "start", "start(1)")
    a.actor.send( m )
	kotlinx.coroutines.delay( 600000 )
}