package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage

class ActorKotlinNaive(  name : String) : ActorBasicKotlin(  name  ) {
    override suspend fun actorBody(msg: ApplMessage) {
        showMsg("$msg")
    }

}