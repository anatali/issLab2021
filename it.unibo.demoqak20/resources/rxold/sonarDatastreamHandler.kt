package rxold
/*
*/

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
 
class sonarDatastreamHandle (name : String ,
							 scope : CoroutineScope = GlobalScope ) : ActorBasic(name, scope){

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
 		val vStr  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
 			//println("   $name |  handles msg= $msg  vStr=$vStr")
 			val m1 = MsgUtil.buildEvent(name, "sonarRobot", "sonar($vStr)")
//			println("   ${name} |  propagates m1= $m1")
	 		emitLocalStreamEvent( m1 )  	//PROPAGATE to the pipe
	}

}