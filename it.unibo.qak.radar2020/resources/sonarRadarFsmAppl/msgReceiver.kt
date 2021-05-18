package sonarRadarFsmAppl
/*
 msgReceiver.kt
 ------------------------------------------------------------------------------------------
 ------------------------------------------------------------------------------------------
*/
import it.unibo.kactor.ActorBasicFsm
import highLevelComms.hlComm
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.Dispatchers

fun curThread() : String { return "thread=${Thread.currentThread().name} of ${Thread.activeCount()} threads"  }

 
class msgReceiver( val destActor : ActorBasicFsm, val hlCommSupport : hlComm, val port : Int  ){
/*
 Waits for a msg (a string rep of ApplMessage) on the given hlCommSupport
 and sends the received msg to the given destActor
*/	
	init{
		println("msgReceiver | CREATED on port=$port ")
		GlobalScope.launch(Dispatchers.IO){ work() }
	}
	
	suspend fun  work(){
  		while( true ){
			//println("answerreceiver | waiting ... ")
  			try{
				val msg = hlCommSupport.receive()
				//println("msgReceiver |  $msg")
				val currentMsg = ApplMessage( msg )
	 			MsgUtil.sendMsg( currentMsg, destActor )
  			}catch( e : Exception){
  				//println("msgReceiver | ERROR ${e}")
				val errortMsg = MsgUtil.buildEvent("msgReceiver","failure","connection(end)")
				MsgUtil.sendMsg( errortMsg, destActor )
				break
  			}
		} 			
	}
} 
