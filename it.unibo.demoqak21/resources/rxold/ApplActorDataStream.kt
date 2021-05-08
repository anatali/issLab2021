package rxold
/*
 A base class for application-defined reactive actors
 */
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Struct
import alice.tuprolog.Term
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.CoroutineScope

abstract class ApplActorDataStream(
	name : String, scope : CoroutineScope = GlobalScope ) : ActorBasic(name, scope){
 	
	//From  C in msg(A,event,S,none,C,N) extract  the argument (distance)
    override suspend fun actorBody(msg: ApplMessage) {
 		val vStr  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
        //println("   $name |  handles msg= $msg  vStr=$vStr")
		elabData( vStr )
	}
	
 	abstract protected suspend fun elabData(data : String );

}