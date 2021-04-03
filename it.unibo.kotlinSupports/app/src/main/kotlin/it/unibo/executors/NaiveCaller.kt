package it.unibo.executors

import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class NaiveCaller(name: String, scope: CoroutineScope) :
        ActorBasicKotlin(name,scope, DispatchType.single) {

    fun getWalkerCmd( ) : String{
        val pathTodo   = "ww"
        val walkerName = "walker"
        return MsgUtil.buildDispatch("main", "start", pathTodo, walkerName ).toString()
    }

    suspend fun  doTCPcall(){
        val factoryProtocol = MsgUtil.getFactoryProtocol(Protocol.TCP)
        val conn = factoryProtocol.createClientProtocolSupport("localhost",ActorContextNaive.portNum)
        println( "$name  | doTCPcall $conn ${infoThreads()}" )
        conn.sendALine( getWalkerCmd() )
        //delay(2000)
        //terminate()
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        doTCPcall()
    }
}