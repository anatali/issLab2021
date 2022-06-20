/* Generated by AN DISI Unibo */ 
package it.unibo.demo0

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Demo0 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						discardMessages = true
					}
					 transition( edgeName="goto",targetState="s1", cond=doswitch() )
				}	 
				state("s1") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition(edgeName="t00",targetState="s2",cond=whenDispatch("msg1"))
					transition(edgeName="t01",targetState="s3",cond=whenDispatch("msg2"))
				}	 
				state("s2") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("msg1(ARG)"), Term.createTerm("msg1(ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("s2:msg1:msg1(${payloadArg(0)})")
								delay(1000) 
						}
					}
					 transition(edgeName="t02",targetState="s3",cond=whenDispatch("msg2"))
				}	 
				state("s3") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("msg2(ARG)"), Term.createTerm("msg2(1)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("s3:msg2:msg2(${payloadArg(0)})")
						}
					}
					 transition( edgeName="goto",targetState="s1", cond=doswitch() )
				}	 
			}
		}
}
