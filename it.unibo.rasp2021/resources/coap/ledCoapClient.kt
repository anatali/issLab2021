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
 
object ledCoapClient {

    private val client      = CoapClient()	
	private val ipaddr      = "192.168.1.9:8075"		//5683 default
	private val context     = "ctxblses1"
 	private val destactor   = "led"
 
	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("ledCoapClient | START uriStr: $uriStr")
       client.uri = uriStr
	}

	fun work(){
		doObserve()
		for( i in 1..5 ){
			println("ledCoapClient | turnon")
			sendToLedOnRasp("on")
			Thread.sleep(500)
			println("ledCoapClient | turnoff")
			sendToLedOnRasp("off")
			Thread.sleep(500)
		}
	} 
	
	fun sendToLedOnRasp(move: String) {
		val r       = MsgUtil.buildDispatch("coapclient", "ledCmd", "ledCmd($move)", "led" )
		val respPut = client.put(r.toString(), MediaTypeRegistry.TEXT_PLAIN)
		println("PUT ${r} RESP-CODE=${respPut.code} ${respPut.getResponseText()}")	
	}

	
	fun doObserve(){
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("ledCoapClient | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("ledCoapClient | FAILED")
            }
        })				
	}
 }

 
 fun main( ) {
		ledCoapClient.init()
	    ledCoapClient.work()
		//System.`in`.read()   //to avoid exit
 }

