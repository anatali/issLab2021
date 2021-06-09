package rx
 

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import robotVirtual.virtualrobotSonarSupportActor

 
class dataCleaner (name : String ) : ActorBasic( name ) {
val LimitLow  = 2	
val LimitHigh = 150
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
		if( msg.msgId() != virtualrobotSonarSupportActor.eventId ) return //AVOID to handle other events
  		elabData( msg )
 	}

 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun elabData( msg: ApplMessage ){ //OPTIMISTIC		 
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
  		//println("$tt $name |  data = $data ")
		try{
			val Distance = Integer.parseInt( data ) 
	 		if( Distance > LimitLow && Distance < LimitHigh ){
				emitLocalStreamEvent( msg ) //propagate
	     	}else{
				println("$tt $name |  DISCARDS $Distance ")
	 		}				
		}catch(e: Exception){
			println("$tt $name |  WARNING ${e} ")
		}
	}
}