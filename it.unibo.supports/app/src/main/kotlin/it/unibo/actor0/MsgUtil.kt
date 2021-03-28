package it.unibo.actor0
//FILE MsgUtil.kt

enum class Protocol {
    SERIAL, TCP, UDP, BTH
}

 
object MsgUtil {
var count = 1;
@JvmStatic    fun buildDispatch( actor: String, msgId : String ,
                       content : String, dest: String ) : ApplMessage {
        return ApplMessage(msgId, ApplMessageType.dispatch.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic    fun buildRequest( actor: String, msgId : String ,
                       content : String, dest: String ) : ApplMessage {
        return ApplMessage(msgId, ApplMessageType.request.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic    fun buildReply( actor: String, msgId : String ,
                      content : String, dest: String ) : ApplMessage {
        return ApplMessage(msgId, ApplMessageType.reply.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic    fun buildReplyReq( actor: String, msgId : String ,
                    content : String, dest: String ) : ApplMessage {
        return ApplMessage(msgId, ApplMessageType.request.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic    fun buildEvent( actor: String, msgId : String , content : String  ) : ApplMessage {
        return ApplMessage(msgId, ApplMessageType.event.toString(),
            actor, "none", "$content", "${count++}")
    }
	
//@kotlinx.coroutines.ObsoleteCoroutinesApi
//@kotlinx.coroutines.ExperimentalCoroutinesApi
//@JvmStatic suspend fun sendAMsg( sender : String, msgId: String, msg: String, destActorName: String) {
//		val a = sysUtil.getActor(destActorName)
//        val dispatchMsg = buildDispatch(sender, msgId, msg, destActorName)
//        //println("sendMsg $dispatchMsg")
//        if( a != null ) a.actor.send( dispatchMsg )
//    }
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@JvmStatic    suspend fun sendMsg( 
            sender : String, msgId: String, msg: String, destActor: ActorBasicKotlin) {
        val dispatchMsg = buildDispatch(sender, msgId, msg, destActor.name)
        //println("sendMsg $dispatchMsg")
        destActor.actor.send( dispatchMsg )
    }
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@JvmStatic    suspend fun sendMsg(msg: ApplMessage, destActor: ActorBasicKotlin) {
        destActor.actor.send(msg)
    }
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@JvmStatic    suspend fun sendMsg(
            msgId: String, msg: String, destActor: ActorBasicKotlin, source:String="unknown") {
        val dispatchMsg = buildDispatch(source, msgId, msg, destActor.name)
        //println("sendMsg $dispatchMsg")
        destActor.actor.send(dispatchMsg)
    }


	
	



	

@JvmStatic    fun strToProtocol( ps: String): Protocol {
        //var p: Protocol
        when( ps.toUpperCase() ){
            Protocol.TCP.toString() -> return Protocol.TCP
            Protocol.UDP.toString() -> return Protocol.UDP
            Protocol.SERIAL.toString() -> return Protocol.SERIAL
            else -> return Protocol.TCP
        }
     }
}