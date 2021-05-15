/*
 ResourceCoapClient.kt
 */
package coap

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler

object ch : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("CoapHandler | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("CoapHandler | FAILED")
            }
        } 
object ResourceCoapClient {

    private val client = CoapClient()
	
	private val ipResource  = "localhost:8048"
	private val context     = "ctxresource"
	private val sendactor   = "coapalien"
	private val destactor   = "resource"
	private val msgId       = "cmd"
	
	private val resorcePath = "$context/destactor"
	private var counter     = 0
	
	fun init(){
       val uriStr = "coap://$ipResource/$context/$destactor"
       client.uri = uriStr
	   client.observe(ch)
	}

	fun observe(){
        client.observe(ch)				
	}
	
	fun sendToServer(move: String) {
		if( move == "h" ){
			client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
			//println("GET RESPONSE CODE=  ${respGet.code} ${respGet.getResponseText()}")
			return
		}
		
		if( move == "p" ){
			val r       = MsgUtil.buildRequest("coapalien", "cmd", "cmd(${counter++})", "actorcoap" )
			val respPut = client.put(r.toString(), MediaTypeRegistry.TEXT_PLAIN)
			println("PUT ${r} RESPONSE CODE=  ${respPut.code} ${respPut.getResponseText()}")
		}else{
			val d = MsgUtil.buildDispatch("coapalien", "cmd", "cmd($move)", "actorcoap" )
	        val respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN)
	        println("PUT ${d} RESPONSE CODE=  ${respPut.code}")
		}
    }
}

fun console(){
	val read = Scanner(System.`in`)
	print("MOVE (h,w,s,r,l,z,x,a,d,p,q)>")
	var move = read.next()
	while( move != "q"){
		when( move ){
			"h" -> ResourceCoapClient.sendToServer("h")
			"w" -> ResourceCoapClient.sendToServer("w")
			"s" -> ResourceCoapClient.sendToServer("s")
			"r" -> ResourceCoapClient.sendToServer("r")
			"l" -> ResourceCoapClient.sendToServer("l")
			"x" -> ResourceCoapClient.sendToServer("x")
			"z" -> ResourceCoapClient.sendToServer("z")
			"p" -> ResourceCoapClient.sendToServer("p")
			"d" -> ResourceCoapClient.sendToServer("d")
			"a" -> ResourceCoapClient.sendToServer("a")
 			else -> println("unknown")
		}
		print("MOVE (h,w,s,r,l,z,x,a,d,q)>")
		move = read.next()
	}
	print("BYE")
	System.exit(1)
}



fun main( ) = runBlocking  {
		ResourceCoapClient.init()		
		console()
}

