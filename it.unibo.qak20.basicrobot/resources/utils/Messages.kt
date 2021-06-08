package utils

import kotlinx.coroutines.channels.SendChannel
 
import kotlinx.coroutines.GlobalScope
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.launch
 
object Messages{

  
/*
 Forward a dispatch to a destination actor given by reference
*/	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forward(  sender: String, msgId : String, payload: String, dest : ActorBasic ){
	 	//println("forward  msgId: ${msgId} payload=$payload")
		val msg = MsgUtil.buildDispatch(actor=sender, msgId=msgId , content=payload, dest=dest.name)
 		if( ! dest.actor.isClosedForSend) dest.actor.send( msg  )
		else println("WARNING: Messages.forward attempts to send ${msg} to closed ${dest.name} ")
	}


/*
 Emit an event by using a given MQTT support
*/	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun emit(  sender: String, msgId : String, payload: String, actor: ActorBasic  ){		
		val event = MsgUtil.buildEvent(actor=sender, msgId=msgId , content=payload )
		actor.scope.launch{
			actor.emit( event )
		}
	}
			
}