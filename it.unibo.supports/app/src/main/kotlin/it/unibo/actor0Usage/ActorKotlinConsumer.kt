package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import kotlinx.coroutines.CoroutineScope

class ActorKotlinConsumer( name : String, dispatchType : DispatchType) :
                 ActorBasicKotlin(  name,  dispatchType ) {

    override fun actorBody(msg: ApplMessage) {
        //aboutThreads()
        if( msg.msgId == "start") return
        if( msg.msgId == "end"){  println("$name | ENDS"); terminate(); return }
        showMsg("$msg")

    }

}