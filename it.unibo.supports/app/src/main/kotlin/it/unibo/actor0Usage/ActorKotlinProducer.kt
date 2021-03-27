package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ActorKotlinProducer(name : String, val dest : ActorBasicKotlin, dispatchType : DispatchType) :
        ActorBasicKotlin( name, dispatchType ) {

    override fun actorBody(msg: ApplMessage) {
        aboutThreads()
        showMsg("$msg")
        if( msg.msgId == "start") doProduce()
        if( msg.msgId == "end") doEnd()
    }

    fun doProduce(){
        scope.launch {
            dest.forward("info", "item", dest)
            val msg = MsgUtil.buildEvent(name, "production", "info-item")
            updateObservers(msg)
        }
    }

    fun doEnd(){
        scope.launch {
            dest.forward("end", "ok", dest)
            terminate()
        }
    }




}