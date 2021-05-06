package fsm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class demoactor ( scope: CoroutineScope ) : Fsm( "demoactor", scope ){
 	
	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("init") {	
				action { //it:State
 					println("demoactor | START")
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			state("waitcmd") {	
				action { //it:State
 					println("demoactor | waiting for info ")
 				}
				transition( edgeName="t0",targetState="work",    cond=whenDispatch("info") )
				transition( edgeName="t0",targetState="endWork", cond=whenDispatch("end") )
			}
			state("work") {	
				action { //it:State
 					println("demoactor | handles $currentMsg")
 				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			state("endWork") {	
				action { //it:State
 					println("demoactor | END since msg=$currentMsg")
					terminate()
				}
 			}

				
		}//return
	}
}
