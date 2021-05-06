package stepper
//stepper

import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import fsm.Fsm
import kotlinx.coroutines.delay
import robotAppl.basicrobot
import kotlinx.coroutines.runBlocking
import utils.virtualRobotSupportApp
 

var stepCounter = 0 //here for testing
val ndnt ="   "			

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class stepper ( name: String, scope: CoroutineScope,
				usemqtt:Boolean=false, val owner: Fsm?=null,
				discardMessages:Boolean=true ) : Fsm( name, scope, discardMessages, usemqtt){

lateinit var timer : Fsm
lateinit var robot : Fsm
		
	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (Fsm.() -> Unit){
		var duration = 0
		return{
			state("init") {	
				action {
					timer = timer("timer",    scope, usemqtt=false,        owner=myself  )
					robot = basicrobot("basicrobot", scope, usemqtt=false, owner=myself  )
					delay(1500) //give time to start
					virtualRobotSupportApp.setRobotTarget( myself  )
					println("$ndnt stepper STARTED")
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			state("waitcmd") {
				action {					
					//println("$ndnt stepper waits ... ")
				}
				transition( edgeName="t0",targetState="dostep",  cond=whenDispatch("step") )
				transition( edgeName="t1",targetState="docmd",   cond=whenDispatch("cmd") )
				transition( edgeName="t2",targetState="endwork", cond=whenDispatch("end") ) 
			}
			
			state("docmd") {
				action { //REDIRECT THE COMMAND
					forward(  "${currentMsg.MSGID}", "${currentMsg.CONTENT}",  robot)
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
			state("dostep") {
				action {
					println("$ndnt stepper dostep msg=${currentMsg} ")
					forward(  "cmd", "w", robot )
 					forward( "gauge", "${currentMsg.CONTENT}", timer  )					
					memoTime()
 				}
				transition( edgeName="t2",targetState="stepKo",     cond=whenDispatch("sensor")  )   	//(first)
				transition( edgeName="t1",targetState="checkStep",  cond=whenDispatch("endgauge") )
 				transition( edgeName="t3",targetState="endwork", cond=whenDispatch("end")     )
				transition( edgeName="t4",targetState="docmd",   cond=whenDispatch("cmd") )
			}
			
			state("checkStep"){
				action {
					forward(  "cmd", "h", robot )
					println("checkStep  ")
					delay(100)  //give time to perceive a lost collision
 				}
				transition( edgeName="t1", targetState="stepKo",       cond=whenDispatch("sensor")    ) //obstacle lost
				transition( edgeName="t2", targetState="sendDone",  cond=doswitch()                )
			}
/*
 WARNING: The step time could terminate just before that a detected collision is perceived
 */

			state("sendDone"){
				action {
					println("sendToOwner  ")
					if( owner is Fsm ){
						forward( "stepdone", "ok", owner )
					}
				}				
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
				
			
			state("stepKo") {
				action {
					duration=getDuration()-50 //-50 to compensate elab (tricky) 
    			}
 				//WARNING: endgauge  should be consumed
				transition( edgeName="t0",targetState="discardGauge", cond=whenDispatch("endgauge") )
			}
			
			//endgauge could arrive while back in waitcmd 
			state("discardGauge"){
				action{
					//println("$ndnt stepper discardGauge $currentMsg")
					//val duration=getDuration()-100 //to compensate elab (tricky) 	 
					if( owner is Fsm ){
						println("$ndnt stepper discardGauge stepKo at stepCounter=$stepCounter after $duration => stepfail to ${owner.name}")
						forward("stepfail", "$duration", owner)
					}		
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
 									
			state("endwork") {
				action {
 					forward( "end", "end", timer  )
					forward( "end", "end", robot  )
  					terminate()
  				}
 			}	 							
				
		}//return
	}
	
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
//	utils.mqtttraceOn = true
//	val worker = worker( "worker", this )
// 	worker.waitTermination()
	
	val  robot = stepper("stepper", this, usemqtt=true)
	robot.waitTermination()
	println("stepper main ends")
} 
