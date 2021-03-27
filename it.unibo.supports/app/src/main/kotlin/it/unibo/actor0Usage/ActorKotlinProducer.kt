package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.CoroutineScope

class ActorKotlinProducer(name : String, val dest : ActorBasicKotlin,
                          scope: CoroutineScope) : ActorBasicKotlin(name,scope,true,true) {

    override suspend fun actorBody(msg: ApplMessage) {
        aboutThreads()
        showMsg("$msg")
        if( msg.msgId == "start") doProduce()
        if( msg.msgId == "end") doEnd()
    }

    suspend fun doProduce(){
        dest.forward("info", "item", dest)
        val msg = MsgUtil.buildEvent(name,"production","info-item")
        updateObservers(msg)
    }

    suspend fun doEnd(){
        dest.forward("end", "ok", dest)
        terminate()
    }




}