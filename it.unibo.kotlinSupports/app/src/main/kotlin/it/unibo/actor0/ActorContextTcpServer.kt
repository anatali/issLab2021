package it.unibo.actor0
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.*

/*
Works at node level
*/

class ActorContextTcpServer(name:String, scope: CoroutineScope, val protocol: Protocol )
                                        : ActorBasicKotlin( name, scope) {
    protected var hostName: String? = null
    val workTime = 1000L * 6000 //100 min	
    var factoryProtocol : FactoryProtocol

    init {
        System.setProperty("inputTimeOut", workTime.toString() )
        factoryProtocol = MsgUtil.getFactoryProtocol(protocol)
        //val wsSupport = IssWsHttpKotlinSupport.getConnectionWs(scope, IssWsHttpKotlinSupport.WEnvAddr)
        //wsSupport.registerActor(this)   //better to avoid and observe actors rather than support

        /*
        scope.launch(Dispatchers.IO) {
            autoMsg( MsgUtil.startDefaultMsg )
        }*/
        this.sendToYourself(MsgUtil.startDefaultMsg)
    }


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun handleInput(msg: ApplMessage) {
        //println("%%% ActorContextTcpServer $name | READY TO RECEIVE CONNS ")
        waitForConnection()
        println("%%% ActorContextTcpServer | $name receives: $msg conns=${sysUtil.connActive.size}")
        updateExternalCallers(msg.toString())
    }

    private fun updateExternalCallers(msg: String ){

        sysUtil.connActive.forEach{
            //println("%%% ActorContextTcpServer | $name updates: $it  }")
            it.sendALine(msg)
        }
    }

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend protected fun waitForConnection() {
        //We could handle several connections
        //GlobalScope.launch(Dispatchers.IO) {
        scope.launch(sysUtil.userThreadContext) {        //BLOCKS THE SYSTEM if we use DispatchType.single
            try {
                val port = ActorBasicContextKb.portNum
                while (true) {
                    println("%%% ActorContextTcpServer $name | WAIT $protocol-CONNECTION on $port ${infoThreads()}")
                    val conn = factoryProtocol!!.createServerProtocolSupport(port) //BLOCKS
                    sysUtil.connActive.add(conn)
                    handleConnection( conn )
                }
            } catch (e: Exception) {
                 println("      ActorContextTcpServer $name | WARNING: ${e.message}")
            }
        }
    }
/*
EACH CONNECTION WORKS IN ITS OWN COROUTINE
 */
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend protected fun handleConnection(conn: IConnInteraction) {
        //GlobalScope.launch(Dispatchers.IO) {
            //scope.launch(sysUtil.userThreadContext) {
            try {
                sysUtil.traceprintln("%%% ActorContextTcpServer $name | NEWWWWWWWWWW conn:$conn")
                while (true) {
                    val msg = conn.receiveALine()       //BLOCKING
//println("%%% ActorContextTcpServer  $name | msg:$msg in ${sysUtil.aboutThreads(name)}")
                    if( msg != null ) {
                        val inputmsg = ApplMessage.create(msg)
//println("%%% ActorContextTcpServer  $name | inputmsg:$inputmsg  ")
						//sysUtil.updateLogfile( actorLogfileName, inputmsg.toString(), dir=msgLogNoCtxDir )
                        if (inputmsg.isEvent() ) {
                            //propagateEvent(inputmsg)
                            continue
                        }
                        val dest = inputmsg.msgReceiver
//println("%%% ActorContextTcpServer  $name | dest:$dest  ")
                        val existactor = ActorBasicContextKb.hasActor(dest)
                        if (existactor) {
                            try {
                                val actor = ActorBasicContextKb.getActor(dest)!!
//println("%%% ActorContextTcpServer  $name | actor:$actor  ")
                                if (inputmsg.isRequest()) { //Oct2019
                                    //set conn in the msg to the actor
                                    inputmsg.conn = conn
                                }
                                MsgUtil.sendMsg(inputmsg, actor)
                            } catch (e1: Exception) {
                                println("%%% ActorContextTcpServer $name |  ${e1.message}")
                            }
                        } else println("%%% ActorContextTcpServer $name | WARNING!! no local actor ${dest} in ${ActorBasicContextKb.name}")
                    }// msg != null
                    else{
                        conn.closeConnection()
                        sysUtil.connActive.remove(conn)
                        break
                    }
                }
            } catch (e: Exception) {
                println("%%% ActorContextTcpServer $name | handleConnection: ${e.message}")
                sysUtil.connActive.remove(conn)
            }
        //}//scope
    }//handleConnection

/*
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend fun propagateEvent(event : ApplMessage){
         ctx.actorMap.forEach{
             //sysUtil.traceprintln("       ActorContextTcpServer $name | in ${ctx.name} propag $event to ${it.key} in ${it.value.context.name}")
             val a = it.value
             try{
                 a.actor.send(event)
             }catch( e1 : Exception) {
                println("               %%% ActorContextTcpServer $name | propagateEvent WARNING: ${e1.message}")
             }
         }
    }*/
}

