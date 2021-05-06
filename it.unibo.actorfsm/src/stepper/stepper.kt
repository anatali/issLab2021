package stepper
//stepper

import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import fsm.Fsm
import kotlinx.coroutines.delay
import robotAppl.basicrobot
import kotlinx.coroutines.runBlocking
 

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
		
		return{
			state("init") {	
				action {
					timer = timer("timer",    scope, usemqtt=false,        owner=myself  )
					robot = basicrobot("basicrobot", scope, usemqtt=false, owner=myself  )
					println("$ndnt stepper STARTED")
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			state("waitcmd") {
				action {
					//println("$ndnt stepper waits ... ")
				}
				transition( edgeName="t0",targetState="dostep",  cond=whenDispatch("step") )
				transition( edgeName="t0",targetState="docmd",   cond=whenDispatch("cmd") )
				transition( edgeName="t0",targetState="endwork", cond=whenDispatch("end") ) 
			}
			
			state("docmd") {
				action { //REDIRECT THE COMMAND
					forward(  "${currentMsg.MSGID}", "${currentMsg.CONTENT}",  robot)
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
			state("dostep") {
				action {
					//println("$ndnt stepper dostep msg=${currentMsg} owner=${owner.name}")
					forward(  "cmd", "w", robot )
 					forward( "gauge", "${currentMsg.CONTENT}", timer  )					
					memoTime()
 				}
				transition( edgeName="t2",targetState="stepKo",  cond=whenDispatch("sensor")  )   	//(first)
				transition( edgeName="t1",targetState="stepOk",  cond=whenDispatch("endgauge") )
 				transition( edgeName="t3",targetState="endwork", cond=whenDispatch("end")     )
				transition( edgeName="t4",targetState="docmd",   cond=whenDispatch("cmd") )
			}
/*
 WARNING: The step time could terminate just before that a detected collision is perceived
 */
			state("stepOk") {
				//only if the virtualRobotSupportApp agree !!!
				//the stepper should make reference to the virtualRobotSupportApp  state ?!
				action {
					forward(  "cmd", "h", robot )
 					stepCounter++
					if( owner is Fsm ){
						println("$ndnt stepper stepOk stepCounter=$stepCounter  => stepDoneMsg to ${owner.name}")
						forward( "stepdone", "ok", owner )
					} 			
 				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )				
 			}
				
			state("stepKo") {
				action {
					val duration=getDuration()-100 //to compensate elab (tricky) 	 
					if( owner is Fsm ){
						println("$ndnt stepper stepKo at stepCounter=$stepCounter after $duration => stepfail to ${owner.name}")
						forward("stepfail", "$duration", owner)
						delay(100)
					}		
    			}
 				//WARNING: endgauge  should be consumed
				transition( edgeName="t0",targetState="discardGauge", cond=whenDispatch("endgauge") )
			}
			//endgauge could arrive while back in waitcmd 
			state("discardGauge"){
				action{
					println("$ndnt stepper discard $currentMsg")
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
