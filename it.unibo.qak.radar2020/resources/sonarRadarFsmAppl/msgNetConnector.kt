package sonarRadarFsmAppl

import it.unibo.kactor.ActorBasicFsm
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.supports.FactoryProtocol
import highLevelComms.hlComm
import kotlinx.coroutines.delay

class msgNetConnector( val a : ActorBasicFsm ) {
	
	suspend fun connectToRadarGuiServer( port: Int ){
		var conn : IConnInteraction? = null
		val fp  = FactoryProtocol(null,"TCP","robotSonar")
		println("robotSonar | START in ${curThread()} , $fp, $conn")
		while( conn == null ){
		try{
			println("robotSonar | attempt connection ...")
			conn = fp.createClientProtocolSupport("localhost", 8010)
			println("robotSonar | $conn")
			val hlCommSupport = hlComm( conn )
			//activate an answer receiver with the connection just set
			 msgReceiver( a, hlCommSupport!!, port  )	
		}catch( e : Exception){
			println("robotSonar |  FAIL attempt to connect in ${curThread()}... ")
			delay( 500 )
		}
		}		
	}


}