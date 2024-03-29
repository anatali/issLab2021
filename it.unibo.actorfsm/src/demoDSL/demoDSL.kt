package demoDSL

import kotlinx.coroutines.CoroutineScope
import fsm.Fsm
import kotlinx.coroutines.runBlocking
import utils.Messages
import utils.AppMsg
import kotlinx.coroutines.delay

class demoDSL ( name: String, scope:
		CoroutineScope, discardMessages : Boolean = false ) : Fsm( name, scope, discardMessages){
	
override fun getInitialState() : String{
 return "s0"
 }

override fun getBody() : (Fsm.() -> Unit){
  return {
	state("s0") {
		action {   println("demoDSL | state s0 $currentMsg")  }
		transition( edgeName="t0",
			targetState="s1", cond=doswitch())	 //empty move		
	}			
	state("s1") {
		action {   println("demoDSL | state s1 $currentMsg")  }
		transition( edgeName="t1",
			targetState="s2",   cond=whenDispatch("msg1") )
		transition( edgeName="t2",
			targetState="s3", cond=whenDispatch("msg2") )
	}
	state("s2") {
		action {   println("demoDSL | state s2 $currentMsg")  }
		transition( edgeName="t0",
			targetState="s3", cond=whenDispatch("msg2") )
	}
	state("s3") {
		action {   println("demoDSL | state s3 $currentMsg")  }
		transition( edgeName="t0",
			targetState="s1", cond=doswitch())	 //empty move
	}
	}//return
}//getBody 
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun demoDiscardMsgs( scope: CoroutineScope){
	println("---- DEMO with discardMessages=true")
	val demo = demoDSL( "demo", scope, discardMessages=true )
 	delay( 100 )
	Messages.forward("main","msg1","msg1_hello",  demo)
	delay( 100 )
	Messages.forward("main","msg1","msg1_hello", demo)
	delay( 100 )
	Messages.forward("main","msg2","msg2_hello", demo)	
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun demoStoreMsgs( scope: CoroutineScope){
	println("---- DEMO with discardMessages=false")
	val demo = demoDSL( "demo", scope  )
 	delay( 100 )
	Messages.forward("main","msg1","msg1_hello",  demo)
	delay( 100 )
	Messages.forward("main","msg1","msg1_hello", demo)
	delay( 100 )
	Messages.forward("main","msg2","msg2_hello", demo)	
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() = runBlocking(){
	demoStoreMsgs( this ) 
	//demoDiscardMsgs( this )
}