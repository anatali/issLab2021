package it.unibo.sonarguigpring


import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import org.eclipse.californium.core.CoapResponse
 

class connQakCoap( )  {

 var client   : CoapClient = CoapClient(  )
	
	 fun createConnection(  ){
 			val url = "coap://${configurator.hostAddr}:${configurator.port}/${configurator.ctxqadest}/${configurator.qakdest}"
 			println("connQakCoap | url=${url.toString()}")
 			//var uriStr: coap://192.168.1.22:8060/ctxdomains/waiter
			client = CoapClient(  )
		    client.uri = url.toString()
			client.setTimeout( 1000L )
 			//initialCmd: to make console more reactive at the first user cmd
 		    val respGet  = client.get( ) //CoapResponse
			if( respGet != null )
				println("connQakCoap | createConnection doing  get | CODE=  ${respGet.code} content=${respGet.getResponseText()}")
			else
				println("connQakCoap | url=  ${url} FAILURE")
	}
	
	 fun forward( msg: ApplMessage ){		
        println("connQakCoap | PUT forward ${msg}  ")		
        val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        println("connQakCoap | RESPONSE CODE=  ${respPut.code}")		
	}
	
	 fun request( msg: ApplMessage){
 		val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
  		if( respPut != null ) println("connQakCoap | answer= ${respPut.getResponseText()}")		
		
	}
	
	 fun emit( msg: ApplMessage){
//		val url = "coap://$hostIP:$port/ctx$destName"		//TODO
//		client = CoapClient( url )
        //println("PUT emit url=${url} ")		
         val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
         println("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}")		
		
	}
	
	 fun readRep(   ) : String{
		val respGet : CoapResponse = client.get( )
		return respGet.getResponseText()
	}
}