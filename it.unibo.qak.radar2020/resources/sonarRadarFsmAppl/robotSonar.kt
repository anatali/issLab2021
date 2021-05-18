package sonarRadarFsmAppl.robotSonar
/*
 robotSonar.kt
 ------------------------------------------------------------------------------------------
 High-level communications embedded in the hlComm library (working upon lowLevelComms)
 Explicit representation of states and of transitions
 Explicit definition of msgId promoted:
 	robotSonar --> polar : polar( D,A )  --> radarGui
               <-- answer : work(done)  <--
 ------------------------------------------------------------------------------------------
*/
import it.unibo.supports.FactoryProtocol
import it.unibo.kactor.ActorBasicFsm
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import highLevelComms.hlComm
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ApplMessage
import sonarRadarFsmAppl.msgReceiver
import sonarRadarFsmAppl.curThread

class robotSonar( name : String, scope: CoroutineScope ) : ActorBasicFsm( name, scope) {
var hlCommSupport : hlComm?      =  null
var i = 1
	
    override fun getInitialState(): String{ return "INIT"}
	
	suspend fun connectToRadarGuiServer(){
		var conn : IConnInteraction? = null
		val fp  = FactoryProtocol(null,"TCP","robotSonar")
		println("robotSonar | START in ${curThread()} , $fp, $conn")
		while( conn == null ){
		try{
			println("robotSonar | attempt connection ...")
			conn = fp.createClientProtocolSupport("localhost", 8010)
			println("robotSonar | $conn")
			hlCommSupport = hlComm( conn )
			//activate an answer receiver with the connection just set
			 msgReceiver( myself, hlCommSupport!!, 8010  )	
		}catch( e : Exception){
			println("robotSonar |  FAIL attempt to connect in ${curThread()}... ")
			delay( 500 )
		}
		}		
	}
	
	override fun getBody() : (ActorBasicFsm.() -> Unit){
	//println("robotSonar | getBody in ${curThread()}"  )
	return {
		state("INIT") { //this:State
			action { //it:State
				println("robotSonar | START in ${curThread()}, $currentState ")
				connectToRadarGuiServer()
			}
			transition(edgeName="t0",targetState="SENDREQUEST",cond=doswitch() ) //EMPTY MOVE
 		}
		state("SENDREQUEST"){
			action{
				val v = i++*20
				println(  "robotSonar | doing request: $v")
				hlCommSupport!!.request( "robotsonar","polar", "polar($v,0)", "radargui" )
			}
			transition(edgeName="t0",targetState="ELABANSWER",cond=whenReply("answer") )  
		}	 		
		state("ELABANSWER"){
			action{
				println("$name in ${currentState.stateName} | $currentMsg")
 			}
			transition(edgeName="t0",targetState="END",cond=doswitchGuarded({i>3}) )
			transition(edgeName="t1",targetState="SENDREQUEST",cond=doswitchGuarded({i<=3}) )
 		}	 		
		state("END"){
			action{
				println(  "robotSonar | END")
			}
		}  		
	}
  }//getBody
}//class


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
	val cpus = Runtime.getRuntime().availableProcessors();
    println("robotSonar  |  START CPU=$cpus ${curThread()}")
    val rb = robotSonar("robotSonar", this)
}


