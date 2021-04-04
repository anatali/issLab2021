package it.unibo.actor0robot

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import kotlinx.coroutines.CoroutineScope

class NaiveObserver(name: String, scope: CoroutineScope) :
        ActorBasicKotlin(name,scope, DispatchType.single) {

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name NaiveObserver | $msg ${infoThreads()}" )
    }
}