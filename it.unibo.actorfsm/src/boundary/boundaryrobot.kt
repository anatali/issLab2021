package boundary
//boundary
/*
 --------------------------------------------------
 --------------------------------------------------
 */

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import utils.AppMsg
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import utils.Messages
import fsm.Fsm
import stepper.stepper
 
var nStep = 0		//Declared here for testing purpose
var nStop = 0

enum class  RobotState{
	initial, working, endOfStep, stopped, end
}

val stepTime       = "350"	//a step whose space s is the robot length (s=v*stepTime)
val backTime       = 80L	
val pauseStepTime  = 700L	//delay between steps


/*
 ASSUMPTION: the room is empty
*/ 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class boundaryrobot ( name: String, scope: CoroutineScope,
				   usemqtt:Boolean=false,
				   val owner: Fsm?=null,
				   discardMessages:Boolean=true
				 ) : Fsm( name, scope, discardMessages,usemqtt){

lateinit var robot : Fsm
var nstep = 0 	
 
	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("init") {	
				action { //it:State
					println("boundaryrobot | starts")
					robot = stepper("stepper", scope, usemqtt=false, owner=myself  )
 			 		println( mapRoomKotlin.mapUtil.refMapForTesting )
					println("-----------------")
					mapRoomKotlin.mapUtil.showMap()
					delay(2000) //just to look at the reference map and the current map
				} 
				transition( edgeName="t0",targetState="work", cond=whenDispatch("start") )
			}
			state("work") {	
				action { //it:State
					//delay(pauseStepTime)
					forward( "step", stepTime, robot)
				}
				transition( edgeName="t1",targetState="wall",     cond=whenDispatch("stepfail") )
				transition( edgeName="t0",targetState="stepDone", cond=whenDispatch("stepdone") )
			}
			
			state("stepDone"){
				action{ 
					mapRoomKotlin.mapUtil.doMove("w")
					println("boundaryrobot | stepDone")
					mapRoomKotlin.mapUtil.showMap()
					delay(pauseStepTime)	
				}
				transition( edgeName="t0",targetState="work", cond=doswitch() )		
			}
			
			state("wall") {	
				action { //it:State
					nstep++
					//Go to the previous cell
					delay(pauseStepTime)
					var dt = currentMsg.CONTENT.toLong()-backTime  //consider the bascirobot bacsktep
//					if( dt > 100 ) {
  	 					println("boundaryrobot | obstacle at step $nstep ${currentMsg.CONTENT} dt=$dt")
//						forward( "cmd", "s", robot); delay(dt); forward( "cmd", "h", robot)  
//						delay(pauseStepTime)	//important
//					}
					delay(pauseStepTime)
					forward( "cmd", "l", robot)
					delay(pauseStepTime)
					mapRoomKotlin.mapUtil.doMove("l")
					//delay(pauseStepTime)
				}
				transition( edgeName="t0",targetState="work", cond=doswitchGuarded( {nstep<4}  ) )
				transition( edgeName="t1",targetState="end",  cond=doswitchGuarded( {nstep==4} ) )
 			}
			
			state("end") {	
				action { //it:State
					println("boundaryrobot | ends")
					mapRoomKotlin.mapUtil.showMap()
					forward("end","0",robot)
					terminate()
 				}
 			}
			
		}
	}

}//boundary	
	
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main(  ) = runBlocking{
    println("==============================================")
    println("boundary | PLEASE, ACTIVATE WENV ")
    println("==============================================")
 
	val robot = boundaryrobot("boundaryrobot", this)
 	delay(1000) //give time to setup
	Messages.forward("main", "start","ok",robot)
	robot.waitTermination()
 	println("main | ENDS")
} 