/* Generated by AN DISI Unibo */ 
package it.unibo.sendermock

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sendermock ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 val input   = java.util.Scanner(System.`in`)  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("sendermock start")
						forward("sonarrobot", "sonar(10)" ,"sonarresource" ) 
					}
				}	 
				state("work") { //this:State
					action { //it:State
						delay(500) 
						 print("VALUE>")
									var Data = input.nextLine()  
						emit("sonarrobot", "sonar($Data)" ) 
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
