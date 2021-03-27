package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope

class ActorKotlinConsumer(name : String, scope: CoroutineScope) :
                 ActorBasicKotlin(  name, scope, true, true ) {

    override suspend fun actorBody(msg: ApplMessage) {
        //aboutThreads()
        if( msg.msgId == "start") return
        if( msg.msgId == "end"){  println("$name | ENDS"); terminate(); return }
        showMsg("$msg")

    }

}