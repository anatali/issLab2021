package rxold
import it.unibo.kactor.*
import alice.tuprolog.*
import java.io.File

/*
 * ----------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------
 */

class dataConsumer( name : String ) : ActorBasic( name ){  
    init{
		println("$tt $name | start in ctx=${this.context}")
	}		  		      
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
 		println("$tt $name | received  $msg "  )
// 		emit( msg, avatar=true ) 				 
    }
}