package it.unibo.executors

import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope

class NaiveCaller(name: String, scope: CoroutineScope) :
        ActorBasicKotlin(name,scope, DispatchType.single) {

    fun getWalkerCmd( ) : String{
        val pathTodo   = "ww"
        val walkerName = "walker"
        return MsgUtil.buildDispatch("main", "start", pathTodo, walkerName ).toString()
    }

    fun doTCPcall(){
        val factoryProtocol = MsgUtil.getFactoryProtocol(Protocol.TCP)
        val conn = factoryProtocol.createClientProtocolSupport("localhost",ActorContextNaive.portNum)
        println( "$name  | doTCPcall $conn ${infoThreads()}" )
        conn.sendALine( getWalkerCmd() )
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        doTCPcall()
    }
}