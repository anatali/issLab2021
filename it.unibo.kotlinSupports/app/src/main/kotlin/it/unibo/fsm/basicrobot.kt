package it.unibo.fsm
import kotlinx.coroutines.CoroutineScope
import fsm.FsmBasic
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

  

 


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class basicrobot (name: String, scope: CoroutineScope,
				  usemqtt:Boolean=false,
				  val owner: FsmBasic?=null,
				  discardMessages:Boolean=true
				 ) : FsmBasic( name, scope, discardMessages,usemqtt){
	enum class basicrobotstate {
		stop, forward, backward, rleft, rright, obstacle
	}
	val ndnt   		= "&&& "
	val backTime    = 80L

	companion object{ 
		var rstate = basicrobotstate.stop	//here for testing purpose		
	}
	
	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (FsmBasic.() -> Unit){
		return { //this:Fsm
			state("init") {	
				action { //it:State
					//virtualRobotSupportApp.traceOn = true
					//virtualRobotSupportApp.setRobotTarget( myself  ) //Configure - Inject
					//virtualRobotSupportApp.initClientConn()
					//fsm.traceOn = true
					rstate = basicrobotstate.stop
					println("$ndnt basicrobot | STARTED in LOGICAL state=$rstate")
					//mqtt.subscribe( myself,  "unibo/qak/natali/basicrobot"  )
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
			state("waitcmd"){
				action { //it:State
					//println("$ndnt basicrobot | waits in LOGICAL state = $rstate")  
 				}
				transition( edgeName="t0",targetState="handlesensor", cond=whenDispatch("sensor") )				
				transition( edgeName="t1",targetState="endwork",      cond=whenDispatch("end")    )				
				transition( edgeName="t2",targetState="execcmd",      cond=whenDispatch("cmd")    )				

			}
			state("execcmd"){
				action { //it:State
					//println("$ndnt basicrobot | exec ${currentMsg} in state=${currentState.stateName}")
					val move = currentMsg.msgContent
					doMove( move )
 				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
 			}
			state("handlesensor"){
				action{
					if( currentMsg.msgContent.startsWith("collision") ){ //defensive
						doMove("s"); delay(backTime); doMove("h")	    //robot reflex for safety ...
						if( owner is FsmBasic ) forward( currentMsg, owner )	//soon to gain time ...
						//println("$ndnt basicrobot | collision $currentMsg - moving back a little ...  ")
  						rstate = basicrobotstate.obstacle
					}
					emit( currentMsg.msgId, currentMsg.msgContent  )  //propagate events to the world
				}
				transition( edgeName="t0",targetState="waitcmd",  cond=doswitch()    )		
			}
			state("endwork") {
				action {
					//virtualRobotSupportApp.terminate()
					println("basicrobot | endwork in LOGICAL state = $rstate")
   					terminate()
  				}
 			}	 							

		}
	}
	
	suspend fun doMove( move: String ){
	  	//virtualRobotSupportApp.doApplMove( move )
	  	when( move ){
	  		"w" -> rstate = basicrobotstate.forward
	  		"s" -> rstate = basicrobotstate.backward
	  		"r" -> rstate = basicrobotstate.rright
	  		"l" -> rstate = basicrobotstate.rleft
	  		"h" -> rstate = basicrobotstate.stop
	  	}
	}

}//basicrobot



@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun demoLocal( scope: CoroutineScope){
	val robot = basicrobot("basicrobot", scope, usemqtt=false, owner=null, discardMessages=true)
	println(" --- demoLocal ---")
			delay(500) //wait for starting ...
			MsgUtil.forward( "main","cmd", "r", robot   )
			delay(500)
			MsgUtil.forward( "main","cmd", "l", robot    )
			delay(500)
			MsgUtil.forward( "main","cmd", "w", robot    )
			delay(2000)
			MsgUtil.forward( "main","end", "local", robot    )
			robot.waitTermination()
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{

	demoLocal( this )
	

	println("main ends")
}