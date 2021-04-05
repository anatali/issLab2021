package it.unibo.actor0

import fsm.FsmBasic
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.interaction.IJavaActor
import it.unibo.supports.FactoryProtocol

//FILE MsgUtil.kt

enum class Protocol {
    TCP, UDP //SERIAL,  BTH
}

 
object MsgUtil {
var count = 1;


    val startDefaultMsg = buildDispatch("msgutil","start", "go", "any" )
    val endDefaultMsg   = buildDispatch("msgutil","end", "do", "any" )

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
@JvmStatic    suspend fun sendMsg( sender : String, msgId: String, msg: String, destActor: IJavaActor) {
        sendMsg(sender, msgId, msg, destActor as ActorBasicKotlin)
    }
        @kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@JvmStatic    suspend fun sendMsg(msg: ApplMessage, destActor: ActorBasicKotlin) {
        destActor.actor.send(msg)
    }
@JvmStatic    suspend fun sendMsg(msg: ApplMessage, destActor: IJavaActor) {
        sendMsg(msg,destActor as ActorBasicKotlin)
    }
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@JvmStatic    suspend fun sendMsg(
            msgId: String, msg: String, destActor: ActorBasicKotlin, source:String="unknown") {
        val dispatchMsg = buildDispatch(source, msgId, msg, destActor.name)
        //println("sendMsg $dispatchMsg")
        destActor.actor.send(dispatchMsg)
    }


//==========================================================

    @JvmStatic    fun getFactoryProtocol(protocol: Protocol) : FactoryProtocol{
        var factoryProtocol : FactoryProtocol
        when( protocol ){
            //Protocol.SERIAL -> println("MsgUtil WARNING: TODO")
            Protocol.TCP , Protocol.UDP -> factoryProtocol =
                FactoryProtocol(null, "$protocol", "actor")
            //else -> println("MsgUtil WARNING: protocol unknown")
        }
        return factoryProtocol
    }

    @JvmStatic    fun getConnection(protocol: Protocol, hostName: String, portNum: Int, clientName:String) : IConnInteraction? {
        when( protocol ){
            Protocol.TCP , Protocol.UDP -> {
                val factoryProtocol = FactoryProtocol(null, "$protocol", clientName)
                try {
                    val conn = factoryProtocol.createClientProtocolSupport(hostName, portNum)
                    return conn
                }catch( e: Exception ){
                    //println("MsgUtil: NO conn to $hostName ")
                    return null
                }
            }
            else -> {
                return null
            }
        }
    }

    /* //TODO
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @JvmStatic    fun getConnectionSerial( portName: String, rate: Int) : IConnInteraction {
        val  factoryProtocol =  FactoryProtocol(null,"${Protocol.SERIAL}",portName)
        val conn = factoryProtocol.createSerialProtocolSupport(portName)
        return conn
    }
    */
    @JvmStatic    fun strToProtocol( ps: String): Protocol {
        //var p: Protocol
        when( ps.toUpperCase() ){
            Protocol.TCP.toString() -> return Protocol.TCP
            Protocol.UDP.toString() -> return Protocol.UDP
            //Protocol.SERIAL.toString() -> return Protocol.SERIAL
            else -> return Protocol.TCP
        }
     }


//============================================================
/*
 Forward a dispatch to a destination actor given by reference
*/
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun forward(  sender: String, msgId : String, payload: String, dest : FsmBasic){
    //println("forward  msgId: ${msgId} payload=$payload")
    val msg = buildDispatch(actor=sender, msgId=msgId , content=payload, dest=dest.name)
    if( ! dest.fsmactor.isClosedForSend) dest.fsmactor.send( msg  )
    else println("WARNING: Messages.forward attempts to send ${msg} to closed ${dest.name} ")
}

}