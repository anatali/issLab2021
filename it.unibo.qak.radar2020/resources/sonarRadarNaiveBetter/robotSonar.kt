package sonarRadarNaiveBetter
/*
 robotSonar.kt
 ------------------------------------------------------------------------------------------
 The details of TCP-based communications are embedded in the lowLevelComms library
 The standard interface IConnInteraction is introduced
 The states are implemented in the control-flow
 
 1) Introduce an explicit representation of states for the FSM
 2) Introduce the concept of high-level communication
 ------------------------------------------------------------------------------------------
*/

import lowLevelComms.tcpConnSupport
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING  
 

//enum class State{ INIT,WORK,SEND_DATA }

class robotSonar{
	var conn  : IConnInteraction 	   
	
	init{
		val socket = lowLevelComms.tecnoSupport.connectAsClient("localhost", 8010)
		conn = tcpConnSupport( socket )
		work()
	}
	
	fun work(){
//STATE: send request
		for( i in 1..3 ){
			val v = i*20
			println(  "robotSonar | doing request: $v")
			conn.sendALine( "$v" )
//STATE: wait anwser
			val answer =  conn.receiveALine()
			println(  "robotSonar | answer=$answer")
		}		
	}
}

fun main() {
 	 robotSonar()
}