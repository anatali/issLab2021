/* Generated by AN DISI Unibo */ 
package it.unibo.taskdeploy

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Taskdeploy ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
			val EsPrefix    =  "es"
			var Counter     =  0
			var Esname      = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
					}
					 transition(edgeName="t00",targetState="assignatask",cond=whenRequest("getmeatask"))
				}	 
				state("assignatask") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("getmeatask(NAME,MATR,MAIL)"), Term.createTerm("getmeatask(NAME,MATR,MAIL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var studentName  = payloadArg(0) 
												var studentbadge = payloadArg(1)
												var studentmail  = payloadArg(2)
												var esindex      = Counter++ % 5 
												Esname           = "$EsPrefix${esindex}"
								kotlinCode.utils.append( "$studentName | $studentbadge | $Esname | $studentmail"  )
								println("taskdeploy | assignatask to stud=$studentName badge=$studentbadge mail=$studentmail Esname=$Esname")
						}
						answer("getmeatask", "tasktodo", "tasktodo($Esname)"   )  
					}
					 transition(edgeName="t01",targetState="assignatask",cond=whenRequest("getmeatask"))
				}	 
			}
		}
}
