package rxold
/*
fcor each (sonar) value V, perfoms some flitering acion
and - if ok - emitLocalStreamEvent   sonarData:sonarData(V)
*/

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
 
class sonardsh (name : String,   val owner : ActorBasic,
		var LastDistance : Int = 0, var maxDelta:Int=2) : ApplActorDataStream( name ) {

	init{
		println("   $name |  STARTS")
 	}
 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override suspend fun elabData( data : String ){
		println("   $name |  data = $data ")
		val Distance = Integer.parseInt( data ) 
 		val delta    = Math.abs( Distance - LastDistance )
  		if( delta >= maxDelta  ){ 
			LastDistance = Distance
 			val m1 = MsgUtil.buildEvent(name, "sonarRobot", "sonar($data)")
//			println("   ${name} |  propagates m1= $m1")
	 		emitLocalStreamEvent( m1 )  	//PROPAGATE to the pipe
    	}else{
			//println("   $name |  DISCARDS $Distance ")
 		}				
	}

}