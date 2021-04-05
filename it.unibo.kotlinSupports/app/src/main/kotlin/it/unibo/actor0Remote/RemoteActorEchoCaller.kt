/*
============================================================
RemoteActorCaller

============================================================
 */

package it.unibo.actor0Remote

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class RemoteActorEchoCaller(name: String, scope: CoroutineScope, val disp: DispatchType) :
        ActorBasicKotlin(name, scope, disp) {

    val factoryProtocol = MsgUtil.getFactoryProtocol(Protocol.TCP)
    lateinit var conn   : IConnInteraction
    lateinit var reader : ConnectionReader

    fun startConn(   ) {
        conn = factoryProtocol.createClientProtocolSupport("localhost", ActorBasicContextKb.portNum)
        println("$name  | startConn $conn  ")
        reader = ConnectionReader("reader",  conn  )
        //reader.registerActor(this)  //if we want to handle the answer from
        reader.send(MsgUtil.startDefaultMsg)
     }

    suspend fun doCall(   ){
        println( "$name  | doCall  ${infoThreads()}" )
        for( i in 1..3 ) {
            val msg = MsgUtil.buildDispatch(name, "info$i", "item$i", "echo").toString()
            conn.sendALine(msg)
            delay(3000)
        }
        terminate()
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        if(msg.msgId == "start"){
            startConn(  )
            doCall(   )
        }else if(msg.msgId == "end"){
             //conn.closeConnection()
             terminate()  //also the connection and the reader ...
        }
    }


}