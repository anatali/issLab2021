package sonarRadarFsmAppl
/*
 msgNetServer.kt
 ------------------------------------------------------------------------------------------
 ------------------------------------------------------------------------------------------
*/
import it.unibo.kactor.ActorBasicFsm
import highLevelComms.hlComm
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.Dispatchers

class msgNetServer( val a : ActorBasicFsm ){
	init{
		val workTime = 600000L
		System.setProperty("inputTimeOut", "$workTime" )  //for the server socket		
	}

	fun waitConnection( protocol: String, port : Int  ) : hlComm {
 		println("msgServer | START A TCP SERVER ON PORT $port and waits for a connection ... ")
		val fp    = FactoryProtocol(null,protocol,"support")		
		var hlconn 	: hlComm? = null
 	        while (hlconn == null) {
	            try {
	 			 	val conn : IConnInteraction = fp.createServerProtocolSupport( port ) //waitforconnection
					hlconn = hlComm( conn )
					println("msgServer | CONNECTED ")
					msgReceiver( a, hlconn, port )
	             } catch (e: Exception) {
	                 println("msgServer | WARNING: ${e.message}")
	             }
	 	    }
			return hlconn
	}
	
	
}

 