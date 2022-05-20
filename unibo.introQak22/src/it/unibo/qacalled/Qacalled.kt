/* Generated by AN DISI Unibo */ 
package it.unibo.qacalled

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Qacalled ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
					}
					 transition(edgeName="t02",targetState="handleRequest",cond=whenRequest("r1"))
				}	 
				state("handleRequest") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("r1(X)"), Term.createTerm("r1(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val Answer = "${currentMsg.msgSender()}_${payloadArg(0)}" 
								 			   ut.outMsg( "${getName()} | answer=$Answer" )
								answer("r1", "a1", "a1($Answer)"   )  
						}
					}
					 transition( edgeName="goto",targetState="init", cond=doswitch() )
				}	 
			}
		}
}
