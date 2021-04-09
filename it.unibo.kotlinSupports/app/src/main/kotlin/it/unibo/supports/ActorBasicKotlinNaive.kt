package it.unibo.supports

import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope

class ActorBasicKotlinNaive(name : String,
                            scope: CoroutineScope, dispatchType : DispatchType=DispatchType.single)
                        : ActorBasicKotlin(  name, scope, dispatchType  ) {

    override suspend fun handleInput(msg: ApplMessage) {
        showMsg("$msg  ${sysUtil.aboutThreads(name)}")
        if( msg.msgId == "end") terminate()
    }

}