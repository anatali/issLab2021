package sonarRadarFsmAppl.radarGui
/*
 radarGui.kt
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
import kotlinx.coroutines.launch
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import sonarRadarFsmAppl.msgReceiver
import sonarRadarFsmAppl.curThread
import sonarRadarFsmAppl.msgNetServer     

class radarGui( name : String, scope : CoroutineScope  ) : ActorBasicFsm( name, scope ) {
var msgSender = ""
var hlCommSupport : hlComm? =  null
/*
If the class has a superclass, the superclass constructor
is called before the class construction logic is executed.
 init block is not actually a separate method.
 Instead, all init blocks together with member property initializers
 are compiled into the code of the constructor, so they should
 rather be considered a part of the constructor.
*/	
	init{
		//receiver(  myself  )		//BLOCKS  : we put it in the intial state
	}
	
 	override fun getInitialState(): String{ return "INIT"}
	
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//println("radarGui | getBody in ${curThread()} ")
		return {
			state("INIT") { //this:State
				action { //it:State	}
					radarPojo.radarSupport.setUpRadarGui()
				}
 				transition(edgeName="t0",targetState="ACTIVATE",cond=doswitch() ) //EMPTY MOVE
 			}
			state("ACTIVATE"){
				action {
					//Activate a receiver of ApplMessage and get the connection
 					hlCommSupport = msgNetServer(myself).waitConnection("TCP",8010) 
				}
				transition(edgeName="t0",targetState="WAITING",cond=doswitch() ) //EMPTY MOVE
 			}
			state("WAITING"){
				action{
					println("radarGui | WAITS for request in ${curThread()} ")
				}
				transition(edgeName="t0",targetState="ELAB",cond=whenRequest("polar") ) 
 				transition(edgeName="t0",targetState="END", cond=whenEvent("failure") ) //connection reset
			}
			state("ELAB"){ 
				action{
					println("radarGui | elab ${currentMsg} in ${curThread()} ")
					val args = getDistanceAndAngle(currentMsg)
					radarPojo.radarSupport.update(args.first,args.second)
					storeCurrentMessageForReply()  //SINCE a state switch chnages currentMsg
					transition(edgeName="t0",targetState="SENDANSWER",cond=doswitch() ) //EMPTY MOVE
				}
			}
			state("SENDANSWER"){
				action{
					println("radarGui | SENDANSWER to ${msgToReply.msgSender()} in ${curThread()} ")
					delay( 1000 )//simulate some work to do ...
					hlCommSupport!!.answer("answer", "work(done)", "${myself.name}", "${msgToReply.msgSender()}" )
				}
				transition(edgeName="t0",targetState="WAITING",cond=doswitch() ) //EMPTY MOVE 				}
				
			}
			state("END"){
				action{
					println("radarGui | END")
				}
				transition(edgeName="t0",targetState="ACTIVATE",cond=doswitch() )
			}
		}
	}//getBody

/*
 Utility methods
*/
	fun getDistanceAndAngle( applMsg : ApplMessage) : Pair<String,String>{ //polar(20,90)
		val tt          = Term.createTerm( applMsg.msgContent()  ) as Struct
		val DistanceStr = tt.getArg(0).toString()
		val AngleStr    = tt.getArg(1).toString()
		return Pair(DistanceStr,AngleStr)
	}
}//radarGui

 

//@kotlinx.coroutines.ObsoleteCoroutinesApi
//@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking  {
	//System.setProperty("debugOn", "set" ) 	//for noawtsupport
	val cpus = Runtime.getRuntime().availableProcessors();
    println("radarGui  | START CPU=$cpus ${curThread()}")
 	val radar = radarGui( "radarGui", this )
 }