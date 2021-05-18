package sonarRadarFsm
/*
 robotSonar.kt
 ------------------------------------------------------------------------------------------
 The details of TCP-based communications are embedded in the lowLevelComms library
 The standard interface IConnInteraction is introduced
 The states are implemented with an enum
 There is a SINGLE INPUT source for the answer (The TCP connection)
 
  1) Introduce an explicit representation of transitions
 	(eliminate receiveALine in state WAITANSWER)
  2) Introduce the concept of high-level communication
 ------------------------------------------------------------------------------------------
*/

import lowLevelComms.tcpConnSupport
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING  
 
enum class State{ INIT, SENDREQUEST, WAITANSWER, ELABANSWER, END }

class robotSonar{
	var conn  : IConnInteraction? = null  	   
	var state = State.INIT
	var i     = 0
	
	init{
		while( state != State.END ) work()
	}
	
	fun work(){
		when( state ){
			State.INIT -> {
				val socket = lowLevelComms.tecnoSupport.connectAsClient("localhost", 8010)
				conn = tcpConnSupport( socket )
				state = State.SENDREQUEST	
			}
			State.SENDREQUEST -> {
				i++
				val v = i*20
				println(  "robotSonar | doing request: $v")
				conn!!.sendALine( "$v" )
				state = State.WAITANSWER
 			}
			//TODO: avoid the state WAITANSWER
			//we should go to the SENDREQUEST or END when an input (answer(done)) occurs
			State.WAITANSWER -> {
				val answer =  conn!!.receiveALine()	  //SINGLE INPUT
				println(  "robotSonar | answer=$answer")
				state = State.ELABANSWER
			}
			State.ELABANSWER -> {
				if( i <= 3 ) state = State.SENDREQUEST
				else state = State.END							
			}
			State.END -> {
				println(  "robotSonar | END")
			}
 		}
	}//work
}

fun main() {
 	 robotSonar()
}