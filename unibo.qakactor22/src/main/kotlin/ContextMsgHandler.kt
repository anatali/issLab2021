package it.unibo.kactor

import kotlinx.coroutines.runBlocking
import unibo.comm22.ApplMsgHandler
import unibo.comm22.interfaces.IApplMsgHandler
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.utils.ColorsOut


class ContextMsgHandler(name: String) : ApplMsgHandler(name), IApplMsgHandler {


    override fun elaborate(msg: String?, conn: Interaction2021?) {
        ColorsOut.outappl(name + " | elaborate $msg conn= $conn Not yet implemented", ColorsOut.CYAN);
        TODO("Not yet implemented")
    }

    override fun elaborate(msg: IApplMessage, conn: Interaction2021) {
        //ColorsOut.outappl(name + " | elaborate $msg conn= $conn", ColorsOut.CYAN);
        if( msg.isRequest() ) elabRequest(msg,conn);
        else  elabNonRequest(msg,conn);
    }

    protected fun elabRequest(msg: IApplMessage, conn: Interaction2021) {
        ColorsOut.outappl(name + " | elabRequest $msg conn= $conn", ColorsOut.CYAN);
        val requestMsg = ApplMessage(msg.msgId(),msg.msgType(),
            msg.msgSender(), msg.msgReceiver(), msg.msgContent(), msg.msgNum(), conn);
        val senderName = msg.msgSender()
        val actorRepyName: String = "prefix" + senderName
        if (QakContext.getActor(actorRepyName) == null) { //non esiste già
            ActorForReply(actorRepyName, this, conn)
        }
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
}