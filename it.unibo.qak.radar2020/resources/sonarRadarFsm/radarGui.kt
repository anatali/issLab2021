package sonarRadarFsm
/*
 radarGui.kt
 ------------------------------------------------------------------------------------------
 The details of TCP-based communications are embedded in the lowLevelComms library
 The standard interface IConnInteraction is introduced
 The states are implemented with an enum
 There is a SINGLE INPUT source for the answer (The TCP connection)
 
  1) Introduce an explicit representation of transitions
 	(eliminate receiveALine in state WAITREQUEST)
  2) Introduce the concept of high-level communication
 ------------------------------------------------------------------------------------------
*/
import java.net.ServerSocket
import java.io.DataOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING 
import lowLevelComms.tcpConnSupport

enum class RadarState{ INIT, WAITREQUEST, ELAB, SENDANSWER, END  }

class radarGui{
	var conn  : IConnInteraction? = null 	   
	var state = RadarState.INIT
	var DistanceStr = ""
	
	init{
		while( state != RadarState.END){ work() }
	}
	
	fun work(){		
		when( state ){
			RadarState.INIT -> {
				radarPojo.radarSupport.setUpRadarGui()
				val serverSocket = lowLevelComms.tecnoSupport.connectAsReceiver( 8010 )
				val socket       = lowLevelComms.tecnoSupport.acceptAConnection( serverSocket )
				conn = tcpConnSupport( socket )
				state = RadarState.WAITREQUEST		
			}
			//TODO: avoid to write RadarState.WAITREQUEST 
			//we should go to the State.ELAB when an input (DistanceStr) occurs			
			RadarState.WAITREQUEST -> {
				try{
					DistanceStr  = conn!!.receiveALine()  //SINGLE INPUT
					state = RadarState.ELAB
				}catch( e: Exception){
					state = RadarState.END
				}
			} 
			RadarState.ELAB -> {
				radarPojo.radarSupport.update( DistanceStr, "0")
				state = RadarState.SENDANSWER		
			} 
			RadarState.SENDANSWER -> {
				Thread.sleep(1000 ) //simulate some work to do ...
				conn!!.sendALine("answer(done)")
				state = RadarState.WAITREQUEST
			}		
			RadarState.END  -> {
				println("radarGui | END")
			}
		}
	}//work

}
	
fun main() {
	radarGui()
}