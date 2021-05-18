package kotlintcp
//sonarRadarTcpProdCons.kt
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import sonarRadarOnTcp.lowLevelConnect
import highLevelComms.hlComm
import lowLevelComms.tcpConnSupport
import alice.tuprolog.Term
import alice.tuprolog.Struct
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING !!!
import it.unibo.kactor.ApplMessage
 
fun curThread() : String { 
	return "thread=${Thread.currentThread().name}" 
}

/*
 ----------------------------------------------------------------------------
 Structure    : two components (Kotlin coroutines)
 	sonarProducedClient       works a sonar data source
    radarGuiReceiverServer    open a ServerSoket on port 8010
 
 Interaction  :
 	based on a Dispatch ploar:polar(D,A) or
    	  on a Request  ploar:polar(D,A)
 
 Behavior
 	sonarProducedClient       connects via TCP to the server
                              works a sonar data source
 							  forward/request polar on the TCP connection
                              waits for an answer if polar is a request
    radarGuiReceiverServer    open a ServerSoket on port 8010
                              waits for a connection (JUST ONE!)
                              receives polar and shows the data
                              sends an asnwer if polar is a request
 
 Each component works as a  FSM  with NO EXPLICT representation of states.
 The states are just exposed as COMMENTS
 ----------------------------------------------------------------------------
*/ 
var hlCommClientSupport : hlComm? =  null
var hlServerCommSupport : hlComm? =  null

@kotlinx.coroutines.ExperimentalCoroutinesApi
val sonarProducedClient = GlobalScope.launch{
	//STATE init
	println("sonarClient | START ")
	var conn : IConnInteraction? = null
	while( conn == null ){
		try{
			conn = lowLevelConnect("localhost", 8010 )
		    hlCommClientSupport = hlComm( conn )
		}catch( e : Exception){
			println("sonarClient |  attempt to connect ... ")
			delay( 500 )
		}
	}
	//STATE: work
	for( i in 1..3 ){
		delay( 1000 )
		//STATE send a Dispatch  
		println("sonarClient | sending Dispatch $i in ${curThread()} ")
		hlCommClientSupport!!.forward("sonar","polar","polar(${i*20},0)","radarguiserver")
 	}
	delay( 1000 )
	for( i in 1..3 ){
 		//STATE: send a Request  
		println("sonarClient | sending Request $i in ${curThread()} ")
		hlCommClientSupport!!.request("sonar","polar","polar(${i*30},90)","radarguiserver")
		//STATE: wait the answer
		println("sonarClient | wait for the answer in ${curThread()} ")
		val answer = hlCommClientSupport!!.receive()
		println("sonarClient | answer= $answer in ${curThread()} ")
 	}
}

//val radarGuiConsumer  = GlobalScope.actor<Int> {
//}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val radarGuiReceiverServer  = GlobalScope.launch {
	//STATE init
	println("radarGuiServer | START ")
	radarPojo.radarSupport.setUpRadarGui()	
	val serverSocket    = lowLevelComms.tecnoSupport.connectAsReceiver( 8010 )	

	//STATE waitforconnection	
	val socket          = lowLevelComms.tecnoSupport.acceptAConnection(serverSocket)
	val conn            = tcpConnSupport( socket )
 	hlServerCommSupport = hlComm( conn )

	while( true ){
		//STATE: waitfordata
	 	//println("radarGuiServer | waits for message in ${curThread()} ...")
	 	val msg = hlServerCommSupport!!.receive()
		val applMasg    = ApplMessage( msg )
 		println("radarGuiServer | received: $msg type= ${applMasg.msgType()}" )
		
		//STATE: handdledata
		val tt          = Term.createTerm( applMasg.msgContent()  ) as Struct
		val DistanceStr = tt.getArg(0).toString()
		val AngleStr    = tt.getArg(1).toString()
		println("radarGuiServer | update: $DistanceStr, $AngleStr " )
  		radarPojo.radarSupport.update( DistanceStr, AngleStr)
		
		//STATE: send the answer in case of request
		if( applMasg.isRequest() ){ 
			delay( 1500 )  //Simulate some complex elaboration ...
			hlServerCommSupport!!.answer("answerTopolar", "done")
		}
	}

}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("sonarRadarTcpProdCons| BEGIN")
	radarGuiReceiverServer.join()
    println("sonarRadarTcpProdCons| END")
}