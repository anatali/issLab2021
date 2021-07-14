package it.unibo.qak21.basicrobot

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

object CoapObserverForTesting {
   private val client  = CoapClient()
   private var handler : CoapHandler? = null
	
   fun addObserver(name: String, observed : String, channel : Channel<String>, expected:String?=null ){
	   val uriStr = "coap://localhost:8020/ctxbasicrobot/$observed"   
	   println("	%%%%%% $name | START uriStr: $uriStr expected=$expected")
       client.uri = uriStr
       client.observe( object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("	%%%%%% $name | content=$content  RESP-CODE=${response.code} " )
				if( expected != null && ! content.contains(expected) ) {
	              println("	%%%%%% $name | NOOOOOOOOOOOOOOOOOOOOOOOOOOOO " )
				  runBlocking{ channel.send("no") }				
				}else runBlocking{ channel.send(content) }
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