package it.unibo.actor0

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

    /*
    protected var msgId: String = ""
    protected var msgType: String? = null
    protected var msgSender: String = ""
    protected var msgReceiver: String = ""
    protected var msgContent: String = ""
    protected var msgNum: Int = 0
*/
    //var conn : IConnInteraction? = null   //Oct2019

    /*
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
   */

/*
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
*/

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
