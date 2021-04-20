package it.unibo.actor0

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.robotService.ApplMsgs

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
        @JvmStatic
        //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
        fun create(msg: String) : ApplMessage{
            //sysUtil.colorPrint( "ApplMessage | CREATE: $msg " )
            val jsonContent = msg.split("{")[1].split("}")[0]
            val content = "{$jsonContent}"
            //sysUtil.colorPrint( "ApplMessage | jsonContent: $jsonContent " )
            val msgNoContent = msg.replace(jsonContent,"xxx")  //xxx will become item[4]
            //sysUtil.colorPrint( "ApplMessage | msgNoContent=$msgNoContent " )
            val body  = msgNoContent.replace("msg","").replace("(","").replace(")","")
            val items = body.split(",")
            return ApplMessage(items[0],items[1], items[2], items[3], content, items[5] )
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
        return "msg($msgId,$msgType,$msgSender,$msgReceiver,$msgContent,${msgNum})"
        //if (msgType == null) "msg(none,none,none,none,none,0)" else

    }



}//ApplMessage

//just to test
fun main(){

    //val m = ApplMessage.create( ApplMsgs.testCreateMsg().toString() )
    val m = "msg(robotmove,dispatch,actortry,stepRobot,{\"robotmove\":\"turnLeft\", \"time\": 300},1)"
    println(m)

}