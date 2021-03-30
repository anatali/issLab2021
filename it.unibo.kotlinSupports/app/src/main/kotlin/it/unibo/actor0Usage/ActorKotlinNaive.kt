package it.unibo.actor0Usage

import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope

class ActorKotlinNaive(  name : String, scope: CoroutineScope, dispatchType : DispatchType) //, dispatchType : DispatchType
                        : ActorBasicKotlin(  name, scope, dispatchType  ) {

    override suspend fun handleInput(msg: ApplMessage) {
        showMsg("$msg  ${sysUtil.aboutThreads(name)}")
    }

}