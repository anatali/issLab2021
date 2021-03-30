package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.CoroutineScope

class ActorKotlinConsumer( name : String, scope: CoroutineScope ) :
                 ActorBasicKotlin(  name, scope  ) { //, DispatchType.cpubound

    override suspend fun handleInput(msg: ApplMessage) {
        if( msg.msgId == "start") return
        if( msg.msgId == "end"){  println("$name | ENDS"); terminate(); return }
        showMsg("$msg ${sysUtil.aboutThreads(name)}" )
    }

}