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

    override suspend fun actorBody(msg: ApplMessage) {
	    if( msg.msgId() == "local_obstacleVirtual" ){
			//Already perceived by the distance_filter too
		}else{
  		    //println("$tt $name | xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  msg = $msg ")
			if( msg.msgId() == "sonarRobot"   )  elabSonarData( msg ) //AVOID to handle other events
  			
		} 
 	}

 	
@kotlinx.coroutines.ObsoleteCoroutinesApi

	  suspend fun elabSonarData( msg: ApplMessage ){ //OPTIMISTIC		 
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
  		//println("$tt $name |  elabData data = $data ")		
		val Distance = Integer.parseInt( data ) 
 		if( Distance > LimitLow && Distance < LimitHigh ){
			emitLocalStreamEvent( msg ) //propagate
     	}else{
			println("$tt $name |  DISCARDS $Distance ")
 		}				
 	}
}