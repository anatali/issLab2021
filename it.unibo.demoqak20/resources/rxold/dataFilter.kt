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

//This actor works without any Context
class dataFilter (name : String, val owner : ActorBasic? = null) : ActorBasic( name ) {
val LimitDistance = 8
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
 		val vStr  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
 		elabData( vStr )
 	}

 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun elabData( data : String ){ //		 
		println("   $name |  data = $data ")		
		val Distance = Integer.parseInt( data ) 
 		if( Distance < LimitDistance ){
	 			val m1 = MsgUtil.buildEvent(name, "obstacle", "obstacle($data)")
				println("   ${name} |  emit m1= $m1")
 				if( owner!==null ) owner!!.emit( m1, avatar=true ) 				//emits the qak event sonarRobot
    	}else{
			//println("   $name |  DISCARDS $Distance ")
 		}				
 	}
}