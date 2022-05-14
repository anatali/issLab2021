/* Generated by AN DISI Unibo */ 
package it.unibo.pathexec

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pathexec ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("pathexec starts")
					}
					 transition(edgeName="t00",targetState="dojob",cond=whenRequest("dopath"))
				}	 
				state("dojob") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						 pathut.setPathFromRequest(currentMsg)  
						println("pathTodo = ${pathut.getPathTodo()}")
					}
					 transition( edgeName="goto",targetState="nextMove", cond=doswitch() )
				}	 
				state("nextMove") { //this:State
					action { //it:State
					}
					 transition(edgeName="t01",targetState="handleAlarm",cond=whenEvent("alarm"))
					transition(edgeName="t02",targetState="pathcompleted",cond=whenDispatch("pathdone"))
					transition(edgeName="t03",targetState="pathfailure",cond=whenDispatch("pathfail"))
					transition(edgeName="t04",targetState="nextMove",cond=whenDispatch("moveok"))
				}	 
				state("handleAlarm") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition(edgeName="t05",targetState="handleAlarm",cond=whenEvent("alarm"))
					transition(edgeName="t06",targetState="pathcompleted",cond=whenDispatch("pathdone"))
					transition(edgeName="t07",targetState="nextMove",cond=whenDispatch("moveok"))
					transition(edgeName="t08",targetState="pathfailure",cond=whenDispatch("movefail"))
				}	 
				state("pathfailure") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("pathexec | END FAIL ")
					}
					 transition(edgeName="t09",targetState="dojob",cond=whenRequest("dopath"))
				}	 
				state("pathcompleted") { //this:State
					action { //it:State
					}
					 transition(edgeName="t010",targetState="dojob",cond=whenRequest("dopath"))
				}	 
			}
		}
}
