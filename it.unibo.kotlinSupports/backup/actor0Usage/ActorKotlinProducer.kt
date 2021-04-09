package it.unibo.actor0Usage

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ActorKotlinProducer(name : String, val consumer : ActorBasicKotlin, scope: CoroutineScope ) :
        ActorBasicKotlin( name, scope  ) {

    override suspend fun handleInput(msg: ApplMessage) {
        showMsg("$msg  ${sysUtil.aboutThreads(name)}" )
        if( msg.msgId == "start") doProduce()
        if( msg.msgId == "end") doEnd()
    }

     fun doProduce(){
        scope.launch {
            consumer.forward("info", "item", consumer)
            val msg = MsgUtil.buildEvent(name, "prod", "info-item")
            updateObservers(msg)
        }
    }

     fun doEnd(){
        scope.launch {
            consumer.forward("end", "ok", consumer)     //terminate the consumer
            terminate()
        }
    }




}