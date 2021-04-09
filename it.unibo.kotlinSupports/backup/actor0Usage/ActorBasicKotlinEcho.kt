/*
============================================================
ActorBasicKotlinEcho
redirects any received messages to its observers
============================================================
 */
package it.unibo.actor0Usage

import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope

class ActorBasicKotlinEcho(name : String, scope: CoroutineScope ) //, dispatchType : DispatchType
                        : ActorBasicKotlin(  name, scope  ) {

    override suspend fun handleInput(msg: ApplMessage) {
        showMsg("$msg  ${sysUtil.aboutThreads(name)}")
        if( msg.msgId == "end") terminate()
        else this.updateObservers( msg )
    }

}