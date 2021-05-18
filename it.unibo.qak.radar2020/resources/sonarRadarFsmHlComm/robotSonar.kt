package sonarRadarFsmHlComm
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

import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING  
import highLevelComms.hlComm
import it.unibo.kactor.ApplMessage
 
class robotSonar( name: String ) : fsmAbstract(name) {
var i = 0
 	
	override fun work(){
		when( state ){
			"INIT" -> {
				//connectAsSender()
				connectAsSender("TCP", "localhost", 8010)
 				transition("", "SENDREQUEST") //EMPTY MOVE
			}
			"SENDREQUEST" -> {
 				val v = i++*20
				println(  "robotSonar | sending request: $v")
				hlCommSupport!!.request( "robotsonar","polar", "polar($v,90)", "radargui" )
 				transition("answer", "ELABANSWER" )
 			}
 			"ELABANSWER" -> {
				//GUARDED transtion ...
				if( i <= 3 ) transition("", "SENDREQUEST")  
				else transition("", "END")   					
			}
			"END" -> {
				println(  "robotSonar | END")
			}
 		}
	}//work
}

fun main() {
 	 robotSonar("robotSonar")
}