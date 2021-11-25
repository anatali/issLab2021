package coap

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.FileInputStream
import it.unibo.kactor.MsgUtil

object sonarDataSimulatorCoap {
lateinit var coapSupport : CoapSupport 
var dataCounter = 1
	
	fun init(){
	    val coapAddr = "coap://localhost:5683"
		val path     = "robot/sonar"
		println("$coapAddr / $path")
 		coapSupport = CoapSupport(coapAddr, path)
		sonarValFromUser()
	}
	
    fun sonarValFromUser(   ){
		while( true ){
			print("\nsonarVal>" )
	    	var data = readLine()
	        //println("data ${dataCounter++} = $data " )
 	        val m = MsgUtil.buildEvent("simulatorcoap", "sonar", "sonar($data)")
	        println("PUT via to CoAP: $m"  )
			if( ! coapSupport.updateResource( m.toString() ) ) println("EMIT failure"  )
		}
     }
}

fun main()  {
 	println("sonarDataSimulatorCoap | START")
	sonarDataSimulatorCoap.init()
 }
 