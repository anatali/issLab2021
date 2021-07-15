package rx
 

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct

 
class dataCleaner (name : String ) : ActorBasic( name ) {
val LimitLow  = 2	
val LimitHigh = 150
	init{
		println("dataCleaner STARTS | LimitLow=$LimitLow LimitHigh=$LimitHigh")
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
  		println("$tt $name | xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  msg = $msg ")
	    if( msg.msgId() == "local_obstacleVirtual" ){
			//emitLocalStreamEvent( msg )	//NO DISTANCE here
		}else{
			if( msg.msgId() == "sonarRobot"   )   //AVOID to handle other events
  			elabData( msg )
		} 
 	}

 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun elabData( msg: ApplMessage ){ //OPTIMISTIC		 
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
  		println("$tt $name |  elabData data = $data ")		
		val Distance = Integer.parseInt( data ) 
 		if( Distance > LimitLow && Distance < LimitHigh ){
			emitLocalStreamEvent( msg ) //propagate
     	}else{
			println("$tt $name |  DISCARDS $Distance ")
 		}				
 	}
}