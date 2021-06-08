package rx
 
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct


 
class distanceFilter (name : String ) : ActorBasic( name ) {
val LimitDistance       = 10
var obstacleFound       = false	
var curSonarDistance	= 0
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
		//println("$tt $name |  $msg")
		if( msg.msgId() != "sonarRobot" ) return //AVOID to handle other events
  		elabData( msg )
 	}

 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun elabData( msg: ApplMessage ){ //msg( "sonarRobor, event, EMITTER, none, sonar(D), N")	  	    
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
   		//println("$tt $name |  data = $data ")
		val Distance = Integer.parseInt( data ) 
/*
 * Emit a sonarRobot event (to test the behavior with MQTT)
 * We should avoid this pattern
*/	
 		if( Distance < LimitDistance && ! obstacleFound ){ //avoid to emit a stream of obstacle events
	 		val m1 = MsgUtil.buildEvent(name, "obstacle", "obstacle($Distance)")
			println("$tt $name |  $m1")
			obstacleFound = true 
			emitLocalStreamEvent( m1 ) //propagate event obstacle
     	}else{
			if( Distance > LimitDistance ) obstacleFound = false
	 		if( curSonarDistance != Distance){
				//println("$tt $name |  $Distance")
				curSonarDistance = Distance
		 	 	val m0 = MsgUtil.buildEvent(name, "sonar", "sonar($Distance)")
		 	 	emit( m0 )			
			}
  		}
   	}
}