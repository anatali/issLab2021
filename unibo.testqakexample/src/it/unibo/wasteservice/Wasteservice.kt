/* Generated by AN DISI Unibo */ 
package it.unibo.wasteservice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wasteservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
						lateinit var Material  : String
						lateinit var TruckLoad : String ;
						
						lateinit var TrolleyPos : String ; //GBox,Pbox,Home,Indoor,Other
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("the wasteservice is waiting..")
						 TrolleyPos = "Home"  
					}
					 transition(edgeName="t00",targetState="handlerequest",cond=whenRequest("depositrequest"))
				}	 
				state("handlerequest") { //this:State
					action { //it:State
						 fun checkdepositpossible(MATERIAL:String,LOAD:String) : Boolean { return true }  
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("depositrequest(MATERIAL,TRUCKLOAD)"), Term.createTerm("depositrequest(MATERIAL,TRUCKLOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												 Material 	= payloadArg(0) ;
												 TruckLoad 	= payloadArg(1) ;
								if(  checkdepositpossible( Material, TruckLoad )  
								 ){request("pickup", "pickup($Material,$TruckLoad)" ,"transporttrolley" )  
								}
								else
								 {answer("depositrequest", "loadrejected", "loadrejected($Material,$TruckLoad)"   )  
								 }
						}
					}
					 transition(edgeName="t01",targetState="handlepickupanswer",cond=whenReply("pickupanswer"))
				}	 
				state("handlepickupanswer") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("pickupanswer(RESULT)"), Term.createTerm("pickupanswer(RESULT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var R = payloadArg(0);  
								if(  R == "done"  
								 ){answer("depositrequest", "loadaccept", "loadaccept($Material,$TruckLoad)"   )  
								}
								else
								 {println("FATAL ERROR")
								 answer("depositrequest", "loadrejected", "loadrejected($Material,$TruckLoad)"   )  
								 }
						}
					}
				}	 
			}
		}
}