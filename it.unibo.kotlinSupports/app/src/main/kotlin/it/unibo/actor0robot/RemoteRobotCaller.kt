package it.unibo.actor0robot

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ConnReader(name: String, val conn: IConnInteraction ) :
    ActorBasicKotlin(name,GlobalScope, DispatchType.iobound) {
    var goon = true

     suspend fun getInput(){
        while( goon ){
            //println( "$name  | getInput $goon ${infoThreads()}" )
            val v = conn.receiveALine()
            println("$name | RECEIVES: $v")
            //msg(stepAnswer,dispatch,stepRobot,ctxServer,{"stepDone":"ok" },9)
            val msg = ApplMessage.create(v)
            //println("$name | RECEIVES: ${msg.msgContent}")
            //val msgJson = JSONObject( msg.msgContent )
            //println("$name | RECEIVES: ${msgJson.has("stepDone")}")
            this.updateObservers( msg )
        }
        terminate()
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        if(msg.msgId == "start"){
            //scope.launch{
                getInput()
            //}
        }else if(msg.msgId == "end"){
            goon = false
        }
    }
}

class RemoteRobotCaller(name: String, scope: CoroutineScope) :
        ActorBasicKotlin(name,scope, DispatchType.single) {

    val factoryProtocol = MsgUtil.getFactoryProtocol(Protocol.TCP)
    lateinit var conn : IConnInteraction

    fun getStepRobotCmd( ) : String{
        val stepperName  = "stepRobot"
        val stepMsg      = ApplMsgs.stepMsg.replace("TIME", "350")
        val startStepMsg = MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, stepperName )
        return startStepMsg.toString()
    }

    fun getWalkerCmd( ) : String{
        val pathTodo   = "ww"
        val walkerName = "walker"
        return MsgUtil.buildDispatch("main", "start", pathTodo, walkerName ).toString()
    }

    fun startConn(   ) {
        conn = factoryProtocol.createClientProtocolSupport("localhost", ActorBasicContextKb.portNum)
        println("$name  | doTCPcall $conn ${infoThreads()}")
        val input = ConnReader("input", conn )
        input.registerActor(this)
        input.send(MsgUtil.startDefaultMsg)
        //startReceiver()   //local
     }

    fun doCall( msg : String ){
        println( "$name  | doCall $msg $conn ${infoThreads()}" )
        conn.sendALine(msg )
    }


    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        if(msg.msgId == "start"){
            //doCcall( getWalkerCmd( ) )
            startConn(  )
            doCall( getStepRobotCmd() )
        }else if( msg.msgId == "stepAnswer") {
            val msgJson = JSONObject( msg.msgContent )
            //println("$name | RECEIVES: ${msgJson.has("stepDone")}")
            if( msgJson.has("stepDone") ){
                //msg(stepAnswer,dispatch,stepRobot,ctxServer,{"stepDone":"ok" },13)
                doCall( getStepRobotCmd() )
            }else if( msgJson.has("stepFail") ){
                //msg(stepAnswer,dispatch,stepRobot,ctxServer,{"stepFail":"201" },16)
                println( "$name  | step failed after ${msgJson.getString("stepFail")}" )
            }
        }

    }

    //==================================================
    var goon = true
    fun startReceiver(){
        GlobalScope.launch {
            while (goon) {
                //println( "$name  | getInput $goon ${infoThreads()}" )
                val v = conn.receiveALine()
                println("$name | RECEIVES: $v")
                val msg = ApplMessage.create(v)
                println("$name | RECEIVES: ${msg.msgContent}")
                val msgJson = JSONObject( msg.msgContent )
                println("$name | RECEIVES: ${msgJson.has("stepDone")}")
            }
            terminate()
        }
    }
}