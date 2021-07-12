package it.unibo.qak21.basicrobot

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

object CoapObserverForTesting {
   private val client = CoapClient()
   private var handler : CoapHandler? = null
	
   fun addObserver(name: String, observed : String, channel : Channel<String>, expected:String ){
	   val uriStr = "coap://localhost:8020/ctxbasicrobot/$observed"   
	   println("$name | STARTTTTTTTTTTTTTTTT uriStr: $uriStr")
       client.uri = uriStr
       client.observe( object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("$name | GET RESP-CODE= " + response.code + " content:" + content)
				if( ! content.contains(expected) ) return
                runBlocking{ channel.send(content) }
 			} 
            override fun onError() {
                println("$name | FAILED")
            }
        })		
	}		 

	fun removeObserver(  ){
		client.delete()
	}
		
}