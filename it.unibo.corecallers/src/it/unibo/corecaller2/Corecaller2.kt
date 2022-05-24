/* Generated by AN DISI Unibo */ 
package it.unibo.corecaller2

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Corecaller2 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						request("cmd", "cmd(caller2)" ,"resourcecore" )  
					}
					 transition(edgeName="t06",targetState="handleReply",cond=whenReply("replytocmd"))
				}	 
				state("handleReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						delay(3000) 
						println("       --- corecaller2 handleReply: emit fire")
						emit("alarm", "alarm(fire)" ) 
					}
				}	 
			}
		}
}
