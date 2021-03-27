package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import it.unibo.actor0.sysUtil

class ActorKotlinNaive(  name : String, dispatchType : DispatchType)
                        : ActorBasicKotlin(  name, dispatchType ) {

    override  fun actorBody(msg: ApplMessage) {
        if( msg.msgId == "end" ) terminate()
        else showMsg("$msg  ${sysUtil.aboutThreads(name)}")
    }

}