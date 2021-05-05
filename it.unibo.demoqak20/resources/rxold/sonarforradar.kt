package rxold

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
 
class sonarforradar (name : String,  val owner : ActorBasic ) : ApplActorDataStream( name ) {
	init{
		println("   $name |  STARTS owner=${owner.name}")
 	}
 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override suspend fun elabData( data : String ){ //
		//println("   $name |  propagates $data ")		
  		val m1 = MsgUtil.buildEvent(name, "polar", "polar($data, 0)")
		println("   ${name} |  emit m1= $m1")
		owner.emit( m1 ) 				//emits the qak event polar
 	}

}