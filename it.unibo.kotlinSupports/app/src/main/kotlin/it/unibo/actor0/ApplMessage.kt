package it.unibo.actor0

import it.unibo.`is`.interfaces.protocols.IConnInteraction

enum class ApplMessageType{
    event, dispatch, request, reply, invitation
}
//msg(guiCmd,dispatch,userConsole,any,stop,1)
open class ApplMessage(
        val msgId: String = "", val msgType: String = ApplMessageType.dispatch.toString(),
        val msgSender: String = "", val msgReceiver: String = "",
        val msgContent: String = "", val msgNum: String = "0"
)  {


    companion object{
        //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
        fun create(msg: String) : ApplMessage{
            val body  = msg.replace("msg","").replace("(","").replace(")","")
            val items = body.split(",")
            return ApplMessage(items[0],items[1], items[2], items[3], items[4],items[5] )
        }
    }

    var conn : IConnInteraction? = null

    fun isEvent(): Boolean{
        return msgType == ApplMessageType.event.toString()
    }
    fun isDispatch(): Boolean{
        return msgType == ApplMessageType.dispatch.toString()
    }
    fun isRequest(): Boolean{
        return msgType == ApplMessageType.request.toString()
    }
    fun isReply(): Boolean{
        return msgType == ApplMessageType.reply.toString()
    }

    override fun toString(): String {
        return getDefaultRep()
    }
    fun getDefaultRep(): String {
        return if (msgType == null)
            "msg(none,none,none,none,none,0)"
        else
            "msg($msgId,$msgType,$msgSender,$msgReceiver,$msgContent,${msgNum})"
    }



}//ApplMessage
