/* Generated by AN DISI Unibo */ 
package it.unibo.robotappl1

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Robotappl1 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "activate"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 val Inmapname   = "map2019.txt"  
			   var PathTodo    =  ""  
			   var MapStr      =  
		"1, 1, 1, 1, 1, 1, 1, @ 1, 1, 1, 1, 1, 1, 1, @ 1, 1, X, 1, 1, 1, 1,  @ 1, 1, 0, 1, 1, 1, 1, @ 1, 1, 0, 1, 1, 1, 1, @ X, X, 0, X, X, X, X, "
		return { //this:ActionBasciFsm
				state("activate") { //this:State
					action { //it:State
						unibo.kotlin.planner22Util.createRoomMapFromTextfile( "$Inmapname"  )
						unibo.kotlin.planner22Util.initAI(  )
						println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu")
						unibo.kotlin.planner22Util.showCurrentRobotState(  )
						forward("setMap", "map($MapStr)" ,"pathexec" ) 
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						unibo.kotlin.planner22Util.setGoal( 1, 1  )
						 PathTodo = unibo.kotlin.planner22Util.doPlan().toString() 
						println("Azioni pianificate: $PathTodo")
						request("dopath", "dopath($PathTodo,somecaller)" ,"pathexec" )  
					}
				}	 
				state("pathok") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
				}	 
				state("pathko") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						unibo.kotlin.planner22Util.showMap(  )
						unibo.kotlin.planner22Util.showCurrentRobotState(  )
					}
				}	 
				state("end") { //this:State
					action { //it:State
						println("BYE")
					}
				}	 
			}
		}
}
