package highLevelComms
//sonarRadarWithConnSupport.kt
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import sonarRadarOnTcp.lowLevelConnect
import highLevelComms.hlComm
import lowLevelComms.tcpConnSupport
import alice.tuprolog.Term
import alice.tuprolog.Struct
import it.unibo.kactor.ApplMessage
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING !!!
import it.unibo.supports.*

var hlCommClientSupport : hlComm? =  null
var hlServerCommSupport : hlComm? =  null


fun curThread() : String { 
	return "thread=${Thread.currentThread().name}" 
}
@kotlinx.coroutines.ExperimentalCoroutinesApi
val sonarProducedClient = GlobalScope.launch{
	//STATE init
	println("sonarClient | START ")
	val fp    = FactoryProtocol(null,"UDP","sonarProducedClient")
	var conn : IConnInteraction? = null
	while( conn == null ){
		try{
			conn = fp.createClientProtocolSupport("localhost", 8010)
 		    hlCommClientSupport = hlComm( conn )
		}catch( e : Exception){
			println("sonarClient |  attempt to connect ... ")
			delay( 500 )
		}
	}

//APPLICATION LEVEL
	//STATE: work
	delay( 2000 ) //required for UDP
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

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val radarGuiReceiverServer  = GlobalScope.launch {
	//STATE init
	println("radarGuiServer | START ")
	radarPojo.radarSupport.setUpRadarGui()
	
	val fp    = FactoryProtocol(null,"UDP","sonarProducedClient")
	//STATE waitforconnection	
	val conn  : IConnInteraction = fp.createServerProtocolSupport( 8010 )
   	hlServerCommSupport = hlComm( conn )

//APPLICATION LEVEL
	//STATE: work
	while( true ){
		//STATE: waitfordata
	 	//println("radarGuiServer | waits for message in ${curThread()} ...")
	 	val msg = hlServerCommSupport!!.receive()
		val applMsg    = ApplMessage( msg )
 		println("radarGuiServer | received: $msg type= ${applMsg.msgType()}" )
		
		//STATE: handdledata
		val tt          = Term.createTerm( applMsg.msgContent()  ) as Struct
		val DistanceStr = tt.getArg(0).toString()
		val AngleStr    = tt.getArg(1).toString()
		println("radarGuiServer | update: $DistanceStr, $AngleStr " )
  		radarPojo.radarSupport.update( DistanceStr, AngleStr)
		
		//STATE: send the answer in case of request
		if( applMsg.isRequest() ){ 
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