package sonarRadarFsmHlComm
/*
 fsmAbstract.kt
 ------------------------------------------------------------------------------------------
 Abstract class that implements the behavior of a FSM with
 states             represented as String
 transitions		as an operation that handles input updating a currentMsg
 
 Defines the abstract operation  work()
 
 1) Make the methods connectAsReceiver()> and connectAsSender() configurable
 according to the protocol, by using a reusable support for layered communications
 as unibonoawtsupports.
 ------------------------------------------------------------------------------------------
*/

import highLevelComms.hlComm
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import it.unibo.supports.FactoryProtocol
import it.unibo.`is`.interfaces.protocols.IConnInteraction
 
 

abstract class fsmAbstract( val name : String ){
	
	var state         : String       = "INIT"
	var currentMsg    : ApplMessage? =  null
	var hlCommSupport : hlComm?      =  null
	
	init{
		val workTime = 600000L
		System.setProperty("inputTimeOut", "$workTime" )  //for the server socket
		while( state != "END"){ work() }
	}
	
	abstract fun work() //TO BE WRITTEN BY THE APPLICATION DESIGNER
	
	fun transition( msgId: String, nextState: String   ){
  		if( msgId.length == 0 ){	//EMPTY move
			state = nextState; return
		}
		try{
			val msg      =  hlCommSupport!!.receive()  //WARNING !!!: too limited
		    currentMsg   = ApplMessage( msg )
			if( msgId.equals( currentMsg!!.msgId() ) ) state = nextState
			else println( "radarGui | input NOT EXPECTED: $msg" )
		}catch( e : Exception ){
			println( "radarGui | WARNING: $e" )
			state = "END"
		}
	}
/*
 TCP-related connections
*/		
	fun connectAsReceiver(){
		val serverSocket = lowLevelComms.tecnoSupport.connectAsReceiver( 8010 )
		val socket       = lowLevelComms.tecnoSupport.acceptAConnection( serverSocket )
		val conn         = lowLevelComms.tcpConnSupport( socket)  //IConnInteraction
		hlCommSupport = hlComm( conn )		
	}
	fun connectAsSender(){
		val socket = lowLevelComms.tecnoSupport.connectAsClient("localhost", 8010)
		val conn   = lowLevelComms.tcpConnSupport(socket) //IConnInteraction
 		hlCommSupport = hlComm( conn )		
	}
/*
 Protocol-configurable connections
*/		
	fun connectAsReceiver( protocol: String, port : Int  ){
		val fp    = FactoryProtocol(null,protocol,name)	 
 		val conn : IConnInteraction = fp.createServerProtocolSupport( port ) //waitforconnection
		hlCommSupport = hlComm( conn )
	}
	
	fun connectAsSender( protocol: String, host: String, port : Int  ){
		val fp    = FactoryProtocol(null,protocol,name)
		var conn : IConnInteraction? = null
		while( conn == null ){
			try{
				 conn          = fp.createClientProtocolSupport(host, port) 
				 hlCommSupport = hlComm( conn )
		    }catch( e : Exception){
				println("sonarClient |  attempt to connect to port=$port ... ")
				Thread.sleep( 500 )
		   }
		}
	}

}