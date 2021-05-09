package kotlinCode

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch 
 
object sentinelCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8055"		//5683 default
	private val context     = "ctxsentinel"
 	private val destactor   = "sentinel"
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( owner: ActorBasic? = null){ 
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("sentinelCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("sentinelCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
           } 
            override fun onError() {
                println("sentinelCoapObserver | FAILED")
            }
        })		
	}

 }

 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
		sentinelCoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }

