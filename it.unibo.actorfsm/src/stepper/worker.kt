package stepper
import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import fsm.Fsm
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class worker ( name: String, scope: CoroutineScope,
				usemqtt:Boolean=false, val owner: Fsm?=null,
				discardMessages:Boolean=true ) : Fsm( name, scope, discardMessages, usemqtt){
	

	override fun getInitialState() : String{
		return "init"
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun getBody() : (Fsm.() -> Unit){
		val steprobot = stepper("stepper", scope, usemqtt=false, owner=myself )
		return{
			state("init") {	
				action { println("worker | START")    }
			    transition( edgeName="t0",targetState="dowork", cond=doswitch() )
			}
			
			state("dowork"){
				action {
					delay(1000)
					forward(  "step", "300", steprobot )
				}
				transition( edgeName="t0",targetState="dowork",    cond=whenDispatch("stepdone") )
				transition( edgeName="t0",targetState="obstacle",  cond=whenDispatch("stepfail") )
			}
			
			state("obstacle"){
				action {
					println( "worker step failed: $currentMsg" )
					forward("end", "normal", steprobot )
					terminate()
				}
			}
		}
	}

}//worker 
 
