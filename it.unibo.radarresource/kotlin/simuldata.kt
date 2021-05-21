package simul

import kotlinx.coroutines.runBlocking
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import javacode.CoapSupport

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
class simuldata( val name : String, val scope: CoroutineScope  )    {
lateinit var coapSupport : CoapSupport	
	init{
		coapSupport = CoapSupport( "coap://localhost:5683","robot/sonar" )
		println("$name init scope=$scope")
	}	
		val actor = scope.actor<ApplMessage>{
 			    for (msg in channel) { // iterate over incoming messages
					println(" --- $name msg=${msg.msgId()}")
			        when ( msg.msgId()  ) {
						"start" -> readInputData() 
			            else -> throw Exception( "unknown" )
			        }
			    }
			}
    suspend fun readInputData(){
		println("readInputDataaaaaaaa") 
        var dataCounter = 1
		var data        = ""
        while( ! data.contains( 'q' ) ){ 
			 print("INPUT>") 
 			 data = readLine()!!
             println("data___${dataCounter++}=$data" )
			 val m = MsgUtil.buildEvent(name, "sonar", "sonar($data)")
             println("EMIT to CoAP: $m"  )
			 if( ! coapSupport.updateResource( m.toString() ) ) println("EMIT failure"  )
		}
    }	
}





@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() {
	 
	 runBlocking {
		println("simulatesonar")
		  
		val a = simuldata("simuldata", this)
		val m = MsgUtil.buildEvent("", "start", "start(1)")
	    a.actor.send( m ) 
		//delay(1000)
	 } 
	 println("simulatesonar BYE")
	
}