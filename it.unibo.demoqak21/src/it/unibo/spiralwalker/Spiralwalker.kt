/* Generated by AN DISI Unibo */ 
package it.unibo.spiralwalker

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Spiralwalker ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		  var stepCounter        = 0
			var CurrentPlannedMove = ""
			val mapname            = "roomMap"
			val maxNumSteps        = 3	  
			val MYSELF             = myself
			
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("&&&  plantester STARTED")
						itunibo.planner.plannerUtil.initAI(  )
						println("INITIAL MAP")
						itunibo.planner.plannerUtil.showMap(  )
						itunibo.planner.plannerUtil.startTimer(  )
					}
					 transition( edgeName="goto",targetState="exploreStep", cond=doswitch() )
				}	 
				state("exploreStep") { //this:State
					action { //it:State
						 stepCounter = stepCounter + 1  
						kotlinCode.pathExec.doJob( "$stepCounter", MYSELF  )
					}
					 transition( edgeName="goto",targetState="waitForAnswer", cond=doswitch() )
				}	 
				state("waitForAnswer") { //this:State
					action { //it:State
						println("waitForAnswer ... ")
					}
					 transition(edgeName="t00",targetState="backToHome",cond=whenDispatch("pathDone"))
				}	 
				state("backToHome") { //this:State
					action { //it:State
						println("backToHome ... ")
						kotlinCode.pathExec.doJob( "0", MYSELF  )
					}
					 transition( edgeName="goto",targetState="waitForBackToHome", cond=doswitch() )
				}	 
				state("waitForBackToHome") { //this:State
					action { //it:State
						println("waitForBackToHome ... ")
					}
					 transition(edgeName="t01",targetState="continueJob",cond=whenDispatch("pathDone"))
				}	 
				state("continueJob") { //this:State
					action { //it:State
						println("MAP AFTER BACK TO HOME $stepCounter")
						itunibo.planner.plannerUtil.showMap(  )
						itunibo.planner.plannerUtil.saveRoomMap( mapname  )
					}
					 transition( edgeName="goto",targetState="exploreStep", cond=doswitchGuarded({ stepCounter < maxNumSteps  
					}) )
					transition( edgeName="goto",targetState="endOfJob", cond=doswitchGuarded({! ( stepCounter < maxNumSteps  
					) }) )
				}	 
				state("endOfJob") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.getDuration(  )
					}
				}	 
			}
		}
}
