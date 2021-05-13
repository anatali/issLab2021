/* Generated by AN DISI Unibo */ 
package it.unibo.led

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Led ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		   
		   var state   = false  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("led started")
						delay(1000) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						println("led waits ...")
					}
					 transition(edgeName="t00",targetState="turnLedOn",cond=whenDispatch("turnOn"))
					transition(edgeName="t01",targetState="turnLedOff",cond=whenDispatch("turnOff"))
				}	 
				state("turnLedOn") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						 state = true 	 
						emit("ledchanged", "ledchanged(on)" ) 
						updateResourceRep( "ledstate($state)"  
						)
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("turnLedOff") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						 state = false 		 
						emit("ledchanged", "ledchanged(off)" ) 
						updateResourceRep( "ledstate($state)"	 
						)
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}