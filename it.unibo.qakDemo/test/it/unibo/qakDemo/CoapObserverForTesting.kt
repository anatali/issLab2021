package it.unibo.qakDemo

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.coap.CoAP

class UpdateHandler(val name : String, val channel : Channel<String>,
					val expected:String?=null) : CoapHandler {
            
	override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("	%%%%%% $name | content=$content  expected=$expected RESP-CODE=${response.code} " )
				/*
                    2.05 means content (like HTTP 200 "OK" but only used in response to GET requests)
 					4.04 means NOT FOUND
				*/
				if( response.code == CoAP.ResponseCode.NOT_FOUND ) return
				//DISCARD the content not related to testing
				if( content.contains("START") || content.contains("created")) return
				if( expected != null &&  content.contains(expected) )
					                     runBlocking{ channel.send(content) }
 			} 
            override fun onError() {
                println("$name | FAILED")
            }
        }

class CoapObserverForTesting(val name: String      = "testingobs",
							 val context: String   = "ctxbundarywalker",
							 val observed : String = "basicboundarywalker",
							 val port: String      = "8022") {
   private var client  = CoapClient()
   private lateinit var handler : CoapHandler
   private val uriStr = "coap://localhost:$port/$context/$observed"   
  
 
	
   fun addObserver(  channel : Channel<String>, expected:String?=null ){
	   println("	%%%%%% $name | START uriStr: $uriStr - expected=$expected"  )
       client!!.uri = uriStr	   
	   handler = UpdateHandler( "h_$name", channel, expected)
	   client!!.observe( handler )
 	}		 
 
	fun terminate(){
		println("	%%%%%% $name | terminate $handler"  )
		//client!!.delete( handler )
		//client!!.shutdown()
		 
	}		
}