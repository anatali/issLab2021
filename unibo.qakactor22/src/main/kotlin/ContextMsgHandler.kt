package it.unibo.kactor

import kotlinx.coroutines.runBlocking
import unibo.comm22.ApplMsgHandler
import unibo.comm22.interfaces.IApplMsgHandler
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.utils.ColorsOut


class ContextMsgHandler(name: String, val ctx: QakContext) : ApplMsgHandler(name), IApplMsgHandler {


    override fun elaborate(msg: String?, conn: Interaction2021?) {
        ColorsOut.outappl(name + " | elaborate $msg conn= $conn Not yet implemented", ColorsOut.CYAN);
        TODO("Not yet implemented")
    }

    override fun elaborate(msg: IApplMessage, conn: Interaction2021) {
        //ColorsOut.outappl(name + " | elaborate $msg conn= $conn", ColorsOut.CYAN);
        if( msg.isRequest() ) elabRequest(msg,conn);
        else if( msg.isEvent()) elabEvent(msg,conn);
            else elabNonRequest(msg,conn);
    }

    protected fun elabRequest(msg: IApplMessage, conn: Interaction2021) {
        ColorsOut.outappl(name + " | elabRequest $msg conn= $conn", ColorsOut.CYAN);
        //Inserisco conn nel messaggio di richiesta
        val requestMsg = ApplMessage(msg.msgId(),msg.msgType(),
            msg.msgSender(), msg.msgReceiver(), msg.msgContent(), msg.msgNum(), conn);

        /*
        val senderName = msg.msgSender()
        val actorRepyName: String = "prefix" + senderName
        if (QakContext.getActor(actorRepyName) == null) { //non esiste già
            ActorForReply(actorRepyName, this, conn)
        }*/
        elabNonRequest(requestMsg, conn)
    }

    protected fun elabNonRequest(msg: IApplMessage, conn: Interaction2021?) {
        ColorsOut.outappl(name + " | elabNonRequest $msg conn= $conn", ColorsOut.CYAN);
        val a = QakContext.getActor(msg.msgReceiver())
        runBlocking {
            if (a != null) MsgUtil.sendMsg(msg, a)
            else ColorsOut.outerr(name + " | I should not be here .. " + msg.msgReceiver())
        }
    }

    protected fun elabEvent(event: IApplMessage, conn: Interaction2021?) {
        ColorsOut.outappl(name + " | elabEvent $event conn= $conn", ColorsOut.CYAN);
        runBlocking {
            ctx.actorMap.forEach {
                //sysUtil.traceprintln("       QakContextServer $name | in ${ctx.name} propag $event to ${it.key} in ${it.value.context.name}")
                val a = it.value
                try {
                    a.actor.send(event)
                } catch (e1: Exception) {
                    println(name + " |  propagateEvent WARNING: ${e1.message}")
                }
            }
        }
    }
}