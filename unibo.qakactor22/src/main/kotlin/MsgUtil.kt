package it.unibo.kactor

import unibo.comm22.interfaces.Interaction2021


//FILE MsgUtil.kt

//import it.unibo.`is`.interfaces.protocols.IConnInteraction
//import it.unibo.supports.FactoryProtocol

enum class Protocol {
    SERIAL, TCP, UDP, BTH
}

 
object MsgUtil {
var count = 1;
@JvmStatic
fun buildDispatch( actor: String, msgId : String ,
                       content : String, dest: String ) : IApplMessage {
        return ApplMessage(msgId, ApplMessageType.dispatch.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic
fun buildRequest( actor: String, msgId : String ,
                       content : String, dest: String ) : IApplMessage {
        return ApplMessage(msgId, ApplMessageType.request.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic
fun buildReply( actor: String, msgId : String ,
                      content : String, dest: String ) : IApplMessage {
        return ApplMessage(msgId, ApplMessageType.reply.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic
fun buildReplyReq( actor: String, msgId : String ,
                    content : String, dest: String ) : IApplMessage {
        return ApplMessage(msgId, ApplMessageType.request.toString(),
            actor, dest, "$content", "${count++}")
    }
@JvmStatic
fun buildEvent( actor: String, msgId : String , content : String  ) : IApplMessage {
        return ApplMessage(msgId, ApplMessageType.event.toString(),
            actor, "none", "$content", "${count++}")
    }
	



@JvmStatic
suspend fun sendMsg( sender : String, msgId: String, msg: String, destActor: ActorBasic) {
        val dispatchMsg = buildDispatch(sender, msgId, msg, destActor.name)
        //println("sendMsg $dispatchMsg")
        destActor.actor.send( dispatchMsg )
    }


@JvmStatic
suspend fun sendMsg(msg: IApplMessage, destActor: ActorBasic) {
        destActor.actor.send(msg)
    }
@kotlinx.coroutines.ObsoleteCoroutinesApi

@JvmStatic
suspend fun sendMsg(msgId: String, msg: String, destActor: ActorBasic) {
        val dispatchMsg = buildDispatch("any", msgId, msg, destActor.name)
        //println("sendMsg $dispatchMsg")
        destActor.actor.send(dispatchMsg)
    }




@JvmStatic
suspend fun sendMsg(  sender: String, msgId : String, payload: String, destName : String, mqtt: MqttUtils ){
		val msg = buildDispatch(actor=sender, msgId=msgId , content=payload, dest=destName )
		if( mqtt.connectDone() ){
			mqtt.publish( "unibo/qak/${destName}", msg.toString() )
		}
	}
	
	
@JvmStatic
fun getFactoryProtocol(protocol: Protocol) : FactoryProtocol?{
        var factoryProtocol : FactoryProtocol? = null
        when( protocol ){
            Protocol.SERIAL -> println("MsgUtil WARNING: TODO")
            Protocol.TCP , Protocol.UDP -> factoryProtocol =
                FactoryProtocol(null, "$protocol", "actor")
            else -> println("MsgUtil WARNING: protocol unknown")
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
	


@JvmStatic
fun getConnectionSerial( portName: String, rate: Int) : Interaction2021 {
        val  factoryProtocol =  FactoryProtocol(null,"${Protocol.SERIAL}",portName)
        val conn = factoryProtocol.createSerialProtocolSupport(portName)
        return conn
    }

@JvmStatic
fun strToProtocol( ps: String):Protocol{
        //var p: Protocol
        when( ps.toUpperCase() ){
            Protocol.TCP.toString() -> return Protocol.TCP
            Protocol.UDP.toString() -> return Protocol.UDP
            Protocol.SERIAL.toString() -> return Protocol.SERIAL
            else -> return Protocol.TCP
        }
     }
}