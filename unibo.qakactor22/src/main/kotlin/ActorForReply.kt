package it.unibo.kactor

import kotlinx.coroutines.CoroutineScope
import unibo.comm22.interfaces.IApplMsgHandler
import unibo.comm22.interfaces.Interaction2021

class ActorForReply( name:  String,
                     val h: IApplMsgHandler, val conn: Interaction2021 ) : ActorBasic( name ) {

    override suspend fun actorBody(msg: IApplMessage) {
        if( msg.isReply() ) h.sendAnswerToClient(msg.toString(), conn);
        context!!.removeInternalActor(this);
    }

}