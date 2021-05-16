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
 
object dcreceiverCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8037"		//5683 default
	private val context     = "ctxdcreceiver"
 	private val destactor   = "dcreceiver"
 

	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("dcreceiverCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("dcreceiverCoapObserver | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("dcreceiverCoapObserver | FAILED")
            }
        })		
	}

 }

 
 fun main( ) {
		dcreceiverCoapObserver.init()
		System.`in`.read()   //to avoid exit
 }

