/* Generated by AN DISI Unibo */ 
package it.unibo.basicboundarywalker

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Basicboundarywalker ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
		val mapname     = "roomBoundary"  		 
		var NumStep     = 0
		var Myself      = myself    
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("&&&  basicboundarywalker ACTIVE ...")
					}
					 transition(edgeName="t00",targetState="work",cond=whenDispatch("start"))
				}	 
				state("work") { //this:State
					action { //it:State
						 NumStep = 0    
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						forward("cmd", "cmd(w)" ,"basicrobot" ) 
					}
				}	 
				state("detectBoundary") { //this:State
					action { //it:State
						 NumStep++  
						println("basicboundarywalker detectBoundary $NumStep")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						updateResourceRep( "detectBoundary step=$NumStep"  
						)
					}
					 transition( edgeName="goto",targetState="doAheadMove", cond=doswitchGuarded({ (NumStep<5)  
					}) )
					transition( edgeName="goto",targetState="boundaryFound", cond=doswitchGuarded({! ( (NumStep<5)  
					) }) )
				}	 
				state("doAheadMove") { //this:State
					action { //it:State
						delay(300) 
						request("step", "step(300)" ,"basicrobot" )  
						updateResourceRep( "moving"  
						)
					}
					 transition(edgeName="t01",targetState="handleAlarm",cond=whenEvent("alarm"))
					transition(edgeName="t02",targetState="stepDone",cond=whenReply("stepdone"))
					transition(edgeName="t03",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						updateResourceRep( itunibo.planner.plannerUtil.getMap()  
						)
						itunibo.planner.plannerUtil.updateMap( "w"  )
						updateResourceRep( "stepDone"  
						)
					}
					 transition( edgeName="goto",targetState="doAheadMove", cond=doswitch() )
				}	 
				state("stepFailed") { //this:State
					action { //it:State
						println("basicboundarywalker | FOUND A WALL at home=${itunibo.planner.plannerUtil.atHome()}")
						updateResourceRep( "stepFailed"  
						)
						if(  ! itunibo.planner.plannerUtil.atHome()  
						 ){itunibo.planner.plannerUtil.wallFound(  )
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "l"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						}
						else
						 {forward("cmd", "cmd(l)" ,"basicrobot" ) 
						 itunibo.planner.plannerUtil.updateMap( "l"  )
						 }
						delay(350) 
						 pathexecutil.waitUser("onwall_$NumStep")  
					}
					 transition( edgeName="goto",targetState="detectBoundary", cond=doswitch() )
				}	 
				state("boundaryFound") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.saveRoomMap( mapname  )
						println("robotmapper | FINAL MAP")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						 println(itunibo.planner.plannerUtil.showMap())  
						forward("mapDone", "mapDone(mapname)" ,"testboundary" ) 
					}
				}	 
				state("handleAlarm") { //this:State
					action { //it:State
						println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA $ActorResourceRep")
						println("$name in ${currentState.stateName} | $currentMsg")
						println("----------------------------------------------------------------")
					}
				}	 
				state("handleAlarmAfterStep") { //this:State
					action { //it:State
						println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP $ActorResourceRep")
						println("$name in ${currentState.stateName} | $currentMsg")
						println("----------------------------------------------------------------")
					}
					 transition( edgeName="goto",targetState="doAheadMove", cond=doswitch() )
				}	 
			}
		}
}
