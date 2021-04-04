/*
============================================================
RemoteActorCaller

============================================================
 */

package it.unibo.actor0Remote

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope

class RemoteActorEchoCaller(name: String, scope: CoroutineScope) :
        ActorBasicKotlin(name,scope, DispatchType.single) {

    val factoryProtocol = MsgUtil.getFactoryProtocol(Protocol.TCP)
    lateinit var conn : IConnInteraction
    val msgToSend =  MsgUtil.buildDispatch(name, "greeting", "hello", "echo" ).toString()

    fun startConn(   ) {
        conn = factoryProtocol.createClientProtocolSupport("localhost", ActorBasicContextKb.portNum)
        println("$name  | startConn $conn ${infoThreads()}")
        val reader = ConnectionReader("reader", conn )
        reader.registerActor(this)
        reader.send(MsgUtil.startDefaultMsg)
     }

    fun doCall( msg : String ){
        println( "$name  | doCall $msg $conn ${infoThreads()}" )
        conn.sendALine(msg )
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        if(msg.msgId == "start"){
            startConn(  )
            doCall( msgToSend )
        }
    }


}