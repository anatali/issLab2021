package sonarRadarFsmHlComm
/*
 radarGui.kt
 ------------------------------------------------------------------------------------------
 High-level communications embedded in the hlComm library (working upon lowLevelComms)
 Explicit representation of states and of transitions
 Explicit definition of msgId promoted:
 	robotSonar --> polar : polar( D,A )  --> radarGui
               <-- answer : work(done)  <--
 ------------------------------------------------------------------------------------------
*/
import highLevelComms.hlComm
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct

class radarGui( name: String ) : fsmAbstract(name) {
 	override fun work(){		
		when( state ){
			"INIT" -> {
				radarPojo.radarSupport.setUpRadarGui()
				//connectAsReceiver()
				connectAsReceiver( "TCP", 8010 )  //Using unibonoawtsupport
 				transition("polar", "ELAB")	
			}
 			"ELAB" -> {
 				val args  = getDistanceAndAngle(currentMsg!!)
 				println("radarGuiServer | update: ${args.first}, ${args.second} " )
		  		radarPojo.radarSupport.update( args.first, args.second)
 				transition("", "SENDANSWER")	//EMPTY MOVE
			} 
			"SENDANSWER" -> {
				Thread.sleep(1000 ) //simulate some work to do ...
				hlCommSupport!!.answer("answer", "work(done)")
 				transition("polar", "ELAB")	
			}		
			"END"  -> {
				println("radarGui | END")
			}
			else -> {
				println("radarGui | State $state UNKWON")
			}
		}
	}//work

/*
 Utility methods
*/
	fun getDistanceAndAngle( applMsg : ApplMessage) : Pair<String,String>{
		val tt          = Term.createTerm( applMsg.msgContent()  ) as Struct
		val DistanceStr = tt.getArg(0).toString()
		val AngleStr    = tt.getArg(1).toString()
		return Pair(DistanceStr,AngleStr)
	}
}
	
fun main() {
	radarGui("radarGui")
}