package sonarRadarNaiveBetter
/*
 radarGui.kt
 ------------------------------------------------------------------------------------------
 The details of TCP-based communications are embedded in the lowLevelComms library
 The standard interface IConnInteraction is introduced
 The states are implemented in the control-flow
 
 1) Introduce an explicit representation of states for the FSM
 2) Introduce the concept of high-level communication
 ------------------------------------------------------------------------------------------
*/
import java.net.ServerSocket
import java.io.DataOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING 
import lowLevelComms.tcpConnSupport

class radarGui{
	var conn  : IConnInteraction 	   
	
	init{
		radarPojo.radarSupport.setUpRadarGui()
		val serverSocket = lowLevelComms.tecnoSupport.connectAsReceiver( 8010 )
		val socket       = lowLevelComms.tecnoSupport.acceptAConnection( serverSocket )
		conn = tcpConnSupport( socket )
		work()
	}
	
	fun work(){
	while( true ){
		try{
//STATE: read the request
			val	DistanceStr   = conn.receiveALine()
//STATE: elaborate the request
			radarPojo.radarSupport.update( DistanceStr, "0")
//STATE: send the answer
			Thread.sleep(1000 ) //simulate some work to do ...
			conn.sendALine("done")
 		}catch( e: Exception){
			println("radarGui | error $e")
			break //exit form while or redo init
		}
	}
		
	}
}
fun main() {
	radarGui()
 	println("radarGui | END")
}