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
 
object ledCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "192.168.1.26:8075"		//5683 default
	private val context     = "ctxblses1"
 	private val destactor   = "led"
 

	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("ledCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("ledCoapObserver | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("ledCoapObserver | FAILED")
            }
        })		
	}
 }

 
 fun main( ) {
		ledCoapObserver.init()
		System.`in`.read()   //to avoid exit
 }

