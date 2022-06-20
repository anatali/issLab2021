/* Generated by AN DISI Unibo */ 
package it.unibo.perceiver

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Perceiver ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("perceiver waits ..")
					}
					 transition(edgeName="t03",targetState="s1",cond=whenEvent("alarm"))
				}	 
				state("s1") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						stateTimer = TimerActor("timer_s1", 
							scope, context!!, "local_tout_perceiver_s1", 100.toLong() )
					}
					 transition(edgeName="t04",targetState="s2",cond=whenTimeout("local_tout_perceiver_s1"))   
					transition(edgeName="t05",targetState="s1",cond=whenEvent("alarm"))
				}	 
				state("s2") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("BYE")
					}
				}	 
			}
		}
}
