package it.unibo.kactor

import alice.tuprolog.Struct
import alice.tuprolog.Term
import it.unibo.`is`.interfaces.protocols.IConnInteraction

enum class ApplMessageType{
    event, dispatch, request, reply, invitation
}

open class ApplMessage  {
    //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
    protected var msgId: String = ""
    protected var msgType: String? = null
    protected var msgSender: String = ""
    protected var msgReceiver: String = ""
    protected var msgContent: String = ""
    protected var msgNum: Int = 0

    var conn : IConnInteraction? = null   //Oct2019

    val term: Term
        get() = if (msgType == null)
            Term.createTerm("msg(none,none,none,none,none,0)")
        else
            Term.createTerm(msgContent)

    //@Throws(Exception::class)
    constructor( MSGID: String, MSGTYPE: String, SENDER: String, RECEIVER: String,
                 CONTENT: String, SEQNUM: String, connection : IConnInteraction? = null ) {
        msgId = MSGID
        msgType = MSGTYPE
        msgSender = SENDER
        msgReceiver = RECEIVER
        msgContent = envelope(CONTENT)
        msgNum = Integer.parseInt(SEQNUM)

        conn   = connection //Oct2019 It is NOT NULL for a request
        //		System.out.println("ApplMessage " + MSGID + " " + getDefaultRep() );
    }

    //@Throws(Exception::class)
    constructor(msg: String) {
        val msgStruct = Term.createTerm(msg) as Struct
        setFields(msgStruct)
    }

    private fun setFields(msgStruct: Struct) {
        msgId = msgStruct.getArg(0).toString()
        msgType = msgStruct.getArg(1).toString()
        msgSender = msgStruct.getArg(2).toString()
        msgReceiver = msgStruct.getArg(3).toString()
        msgContent = msgStruct.getArg(4).toString()
        msgNum = Integer.parseInt(msgStruct.getArg(5).toString())
    }

    fun msgId(): String {
        return msgId
    }

    fun msgType(): String? {
        return msgType
    }

    fun msgSender(): String {
        return msgSender
    }

    fun msgReceiver(): String {
        return msgReceiver
    }

    fun msgContent(): String {
        return msgContent
    }

    fun msgNum(): String {
        return "" + msgNum
    }

    override fun toString(): String {
        return getDefaultRep()
    }

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

    fun getDefaultRep(): String {
        return if (msgType == null)
            "msg(none,none,none,none,none,0)"
        else
            "msg($msgId,$msgType,$msgSender,$msgReceiver,$msgContent,${msgNum()})"
    }

    protected fun envelope(content: String): String {
        try {
            val tt = Term.createTerm(content)
            return tt.toString()
        } catch (e: Exception) {
            return "'$content'"
        }
    }

}//ApplMessage
