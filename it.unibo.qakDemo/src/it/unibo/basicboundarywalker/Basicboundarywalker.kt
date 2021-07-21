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
						println("basicboundarywalker w  ")
						forward("cmd", "cmd(w)" ,"basicrobot" ) 
						delay(500) 
						forward("cmd", "cmd(h)" ,"basicrobot" ) 
						println("basicboundarywalker s  ")
						forward("cmd", "cmd(s)" ,"basicrobot" ) 
						delay(500) 
						forward("cmd", "cmd(h)" ,"basicrobot" ) 
						 sysUtil.waitUser("starting ", 100000)  
					}
					 transition( edgeName="goto",targetState="detectBoundary", cond=doswitch() )
				}	 
				state("detectBoundary") { //this:State
					action { //it:State
						 NumStep++  
						println("basicboundarywalker detectBoundary $NumStep")
					}
					 transition( edgeName="goto",targetState="doAheadMove", cond=doswitchGuarded({ (NumStep<5)  
					}) )
					transition( edgeName="goto",targetState="boundaryFound", cond=doswitchGuarded({! ( (NumStep<5)  
					) }) )
				}	 
				state("doAheadMove") { //this:State
					action { //it:State
						delay(300) 
						request("step", "step(330)" ,"basicrobot" )  
					}
					 transition(edgeName="t01",targetState="stepDone",cond=whenReply("stepdone"))
					transition(edgeName="t02",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.updateMap( "w"  )
					}
					 transition( edgeName="goto",targetState="doAheadMove", cond=doswitch() )
				}	 
				state("stepFailed") { //this:State
					action { //it:State
						println("basicboundarywalker | FOUND A WALL at home=${itunibo.planner.plannerUtil.atHome()}")
						if(  ! itunibo.planner.plannerUtil.atHome()  
						 ){itunibo.planner.plannerUtil.wallFound(  )
						updateResourceRep( "found a wall"  
						)
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "l"  )
						}
						else
						 {forward("cmd", "cmd(l)" ,"basicrobot" ) 
						 itunibo.planner.plannerUtil.updateMap( "l"  )
						 }
						delay(350) 
					}
					 transition( edgeName="goto",targetState="detectBoundary", cond=doswitch() )
				}	 
				state("boundaryFound") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.saveRoomMap( mapname  )
						println("robotmapper | FINAL MAP")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						 println(itunibo.planner.plannerUtil.showMap())  
						updateResourceRep(  "mapdone-${itunibo.planner.plannerUtil.getMap()}"  
						)
						forward("mapDone", "mapDone(mapname)" ,"testboundary" ) 
					}
				}	 
			}
		}
}
