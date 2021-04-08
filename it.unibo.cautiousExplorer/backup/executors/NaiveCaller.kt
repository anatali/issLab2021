package it.unibo.executors

import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope

class NaiveCaller(name: String, scope: CoroutineScope) :
        ActorBasicKotlin(name,scope, DispatchType.single) {

    val factoryProtocol = MsgUtil.getFactoryProtocol(Protocol.TCP)

    fun getWalkerCmd( ) : String{
        val pathTodo   = "ww"
        val walkerName = "walker"
        return MsgUtil.buildDispatch("main", "start", pathTodo, walkerName ).toString()
    }

    fun doTCPWalkercall(){
        val conn = factoryProtocol.createClientProtocolSupport("localhost",ActorBasicContextKb.portNum)
        println( "$name  | doTCPWalkercall $conn ${infoThreads()}" )
        conn.sendALine( getWalkerCmd() )
    }
    fun doTCPStepcall() {
        val stepperName = "stepper"
        val startmsg = MsgUtil.buildDispatch("main", "start", "ok", stepperName).toString()
        val conn = factoryProtocol.createClientProtocolSupport("localhost",ActorBasicContextKb.portNum)
        println( "$name  | doTCPStepcall $conn ${infoThreads()}" )
        conn.sendALine( startmsg )

    }
    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        doTCPStepcall()
    }
}