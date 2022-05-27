/* Generated by AN DISI Unibo */ 
package it.unibo.mapqak22

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Mapqak22 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "activate"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
		val MaxNumStep  = 6
		var NumStep     = 0
		var stepok      = 0
		val StepTime    = 345
		var CurMoveTodo = "h"
		return { //this:ActionBasciFsm
				state("activate") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						 NumStep     = 0;     
						           unibo.kotlin.planner22Util.initAI()
					}
				}	 
				state("coverNextColumn") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t00",targetState="coverColumn",cond=whenReply("stepdone"))
					transition(edgeName="t01",targetState="backHome",cond=whenReply("stepfail"))
				}	 
				state("coverColumn") { //this:State
					action { //it:State
						 stepok = stepok + 1
						   		   unibo.kotlin.planner22Util.updateMap(  "w", "" ) 		
						println("coverColumn stepok=$stepok NumStep=$NumStep")
						delay(300) 
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t02",targetState="coverColumn",cond=whenReply("stepdone"))
					transition(edgeName="t03",targetState="backHome",cond=whenReplyGuarded("stepfail",{ NumStep <  MaxNumStep  
					}))
				}	 
				state("backHome") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						unibo.kotlin.planner22Util.updateMapObstacleOnCurrentDirection(  )
						println("backHome")
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						unibo.kotlin.planner22Util.updateMap( "l", ""  )
						delay(300) 
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						unibo.kotlin.planner22Util.updateMap( "l", ""  )
						delay(300) 
						unibo.kotlin.planner22Util.showCurrentRobotState(  )
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t04",targetState="gotoHome",cond=whenReply("stepdone"))
					transition(edgeName="t05",targetState="fatal",cond=whenReply("stepfail"))
				}	 
				state("gotoHome") { //this:State
					action { //it:State
						 unibo.kotlin.planner22Util.updateMap(  "w", "" ) 
						 		   stepok = stepok - 1 
						println("gotoHome stepok=$stepok")
						delay(300) 
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t06",targetState="gotoHome",cond=whenReplyGuarded("stepdone",{ stepok > 0   
					}))
					transition(edgeName="t07",targetState="turnAndStep",cond=whenReplyGuarded("stepdone",{ stepok == 0  
					}))
					transition(edgeName="t08",targetState="turnAndStep",cond=whenReply("stepfail"))
				}	 
				state("turnAndStep") { //this:State
					action { //it:State
						forward("cmd", "cmd(r)" ,"basicrobot" ) 
						unibo.kotlin.planner22Util.updateMap( "r", ""  )
						delay(300) 
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t09",targetState="posForNextColumn",cond=whenReply("stepdone"))
					transition(edgeName="t010",targetState="endOfWork",cond=whenReply("stepfail"))
				}	 
				state("posForNextColumn") { //this:State
					action { //it:State
						unibo.kotlin.planner22Util.updateMap( "w", ""  )
						forward("cmd", "cmd(r)" ,"basicrobot" ) 
						unibo.kotlin.planner22Util.updateMap( "r", ""  )
						println("posForNextColumn stepok=$stepok")
						unibo.kotlin.planner22Util.showCurrentRobotState(  )
						delay(500) 
					}
					 transition( edgeName="goto",targetState="coverNextColumn", cond=doswitch() )
				}	 
				state("endOfWork") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						  unibo.kotlin.planner22Util.showMap()
						   			var Athome = unibo.kotlin.planner22Util.atHome(); println("athome=$Athome") 
						   			//pathut.saveAdjustedMap("map2019temp")
						   			
					}
					 transition( edgeName="goto",targetState="backToHome", cond=doswitchGuarded({ ! unibo.kotlin.planner22Util.atHome()  
					}) )
					transition( edgeName="goto",targetState="atHomeAgain", cond=doswitchGuarded({! ( ! unibo.kotlin.planner22Util.atHome()  
					) }) )
				}	 
				state("backToHome") { //this:State
					action { //it:State
						unibo.kotlin.planner22Util.setGoal( 0, 0  )
						 val PathTodo = unibo.kotlin.planner22Util.doPlan().toString()  
						println("Azioni pianificate per ritorno finale: $PathTodo")
						pathut.setPath( PathTodo  )
					}
					 transition( edgeName="goto",targetState="nextMove", cond=doswitch() )
				}	 
				state("nextMove") { //this:State
					action { //it:State
						 CurMoveTodo = pathut.nextMove()  
						println("pathexec curMoveTodo= $CurMoveTodo")
					}
					 transition( edgeName="goto",targetState="atHomeAgain", cond=doswitchGuarded({ CurMoveTodo.length == 0  
					}) )
					transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({! ( CurMoveTodo.length == 0  
					) }) )
				}	 
				state("doMove") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						delay(300) 
					}
					 transition( edgeName="goto",targetState="doMoveW", cond=doswitchGuarded({ CurMoveTodo == "w"  
					}) )
					transition( edgeName="goto",targetState="doMoveTurn", cond=doswitchGuarded({! ( CurMoveTodo == "w"  
					) }) )
				}	 
				state("doMoveTurn") { //this:State
					action { //it:State
						forward("cmd", "cmd($CurMoveTodo)" ,"basicrobot" ) 
						unibo.kotlin.planner22Util.updateMap( "$CurMoveTodo", ""  )
						stateTimer = TimerActor("timer_doMoveTurn", 
							scope, context!!, "local_tout_mapqak22_doMoveTurn", 300.toLong() )
					}
					 transition(edgeName="t011",targetState="nextMove",cond=whenTimeout("local_tout_mapqak22_doMoveTurn"))   
				}	 
				state("doMoveW") { //this:State
					action { //it:State
						request("step", "step($StepTime)" ,"basicrobot" )  
						unibo.kotlin.planner22Util.updateMap( "w", ""  )
					}
					 transition(edgeName="t012",targetState="nextMove",cond=whenReply("stepdone"))
					transition(edgeName="t013",targetState="fatal",cond=whenReply("stepfail"))
				}	 
				state("atHomeAgain") { //this:State
					action { //it:State
						  unibo.kotlin.planner22Util.showMap()
						    		unibo.kotlin.planner22Util.saveRoomMap("map2019")
					}
				}	 
				state("fatal") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						  unibo.kotlin.planner22Util.showMap()
						    		unibo.kotlin.planner22Util.saveRoomMap("map2019Failed")
						println("SORRY: fatal error")
					}
				}	 
			}
		}
}
