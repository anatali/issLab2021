/* Generated by AN DISI Unibo */ 
package it.unibo.boundaryqak30

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Boundaryqak30 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var NumStep     = 0  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						 NumStep     = 0;  
						updateResourceRep( "waitingOn( edge_$NumStep )"  
						)
						sysaction { //it:State
						  stateTimer = TimerActor("timer_s0", 
							scope, context!!, "local_tout_boundaryqak30_s0", 2000.toLong() )
						}
					}
					 transition(edgeName="t00",targetState="coverNextEdge",cond=whenTimeout("local_tout_boundaryqak30_s0"))   
					transition(edgeName="t01",targetState="coverNextEdge",cond=whenDispatch("init"))
					interrupthandle(edgeName="t02",targetState="handleStop",cond=whenDispatch("stop"),interruptedStateTransitions)
				}	 
				state("coverNextEdge") { //this:State
					action { //it:State
						delay(500) 
						updateResourceRep( "doingastep $NumStep"  
						)
						request("step", "step(350)" ,"basicrobot" )  
					}
					 transition(edgeName="t03",targetState="coverNextEdge",cond=whenReply("stepdone"))
					transition(edgeName="t04",targetState="otherEdge",cond=whenReply("stepfail"))
					interrupthandle(edgeName="t05",targetState="handleStop",cond=whenDispatch("stop"),interruptedStateTransitions)
				}	 
				state("otherEdge") { //this:State
					action { //it:State
						 NumStep = NumStep + 1  
						println("otherEdge")
						updateResourceRep( "covering( edge_$NumStep)"  
						)
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
					}
					 transition( edgeName="goto",targetState="coverNextEdge", cond=doswitchGuarded({ NumStep < 4  
					}) )
					transition( edgeName="goto",targetState="endOfWork", cond=doswitchGuarded({! ( NumStep < 4  
					) }) )
				}	 
				state("handleStop") { //this:State
					action { //it:State
						 MsgUtil.outmagenta("boundaryqak30 |  stopped")  
						updateResourceRep( "stopped"  
						)
					}
					 transition(edgeName="t06",targetState="exitFromStop",cond=whenDispatch("resume"))
				}	 
				state("exitFromStop") { //this:State
					action { //it:State
						updateResourceRep( "resumed"  
						)
						 MsgUtil.outgreen("boundaryqak30 |  resume")  
						returnFromInterrupt(interruptedStateTransitions)
					}
				}	 
				state("endOfWork") { //this:State
					action { //it:State
						updateResourceRep( "athomeagain"  
						)
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
