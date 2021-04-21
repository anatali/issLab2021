/*
============================================================
ActorContextTcpServer
Accepts a TCP connection on port 8010 and
- redirects an input ApplMessage to the local actor receiver
- if it is set as observer of an actor, redirects to all the connected
  clients the ApplMessage sent by that actor to its observers

============================================================
 */

package it.unibo.actor0Service
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.*
import it.unibo.actor0.sysUtil.connActiveForActor
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.*

class ActorContextTcpServer(name:String, val protocol: Protocol, scope:CoroutineScope   )
                                        : ActorBasicKotlin( name,scope ) {
    protected var hostName: String? = null
    val workTime = 1000L * 6000 //100 min	
    var factoryProtocol : FactoryProtocol
    lateinit var conn : IConnInteraction
    lateinit var connInput : Job

    init {
        System.setProperty("inputTimeOut", workTime.toString() )
        factoryProtocol = MsgUtil.getFactoryProtocol(protocol)
        send(MsgUtil.startDefaultMsg)
    }


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun handleInput(msg: ApplMessage) {
        if( msg.msgId=="start") waitForConnection()
        else {
            //println("%%% ActorContextTcpServer | $name receives: $msg conns=${sysUtil.connActive.size}")
            //updateExternalCallers(msg.toString())
            println("%%% ActorContextTcpServer $name | MMM $msg ")
            answerAndUpdate(msg)
        }
    }

    private fun answerAndUpdate(msg: ApplMessage){
        val dest = msg.msgReceiver
        val conn = connActiveForActor.get(dest)
        //if( conn != null )  conn.sendALine(msg.toString()) //answer only to sender
        //else
            updateExternalCallers(msg.toString())
    }


    private fun updateExternalCallers(msg: String ){
        sysUtil.connActive.forEach{
            println("%%% ActorContextTcpServer | $name updates: $it $msg }")
            it.sendALine(msg)
        }
    }

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend protected fun waitForConnection() {
        //We could handle several connections
        //GlobalScope.launch(Dispatchers.IO) {
        connInput = scope.launch(sysUtil.cpusThreadContext)  {   //sysUtil.userThreadContext  Dispatchers.IO    //BLOCKS THE SYSTEM if we use DispatchType.single
            try {
                val port = ActorBasicContextKb.portNum
                while (true) {
                    println("%%% ActorContextTcpServer $name | WAIT $protocol-CONNECTION on $port ${infoThreads()}")
                    conn = factoryProtocol!!.createServerProtocolSupport(port) //BLOCKS
                    sysUtil.connActive.add(conn)
                    handleConnection( conn )
                }
            } catch (e: Exception) {
                 println("      ActorContextTcpServer $name | WARNING: ${e.message}")
            }
        //}
        }
    }
/*
EACH CONNECTION WORKS IN ITS OWN COROUTINE
 */
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend protected fun handleConnection(conn: IConnInteraction) {
        //GlobalScope.launch(Dispatchers.IO) {
            scope.launch(sysUtil.cpusThreadContext) { //userThreadContext
            try {
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
                        val dest   = inputmsg.msgReceiver
                        val sender = inputmsg.msgSender
                        connActiveForActor.put(sender,conn)
//println("%%% ActorContextTcpServer  $name | dest:$dest  ")
                        val existactor = ActorBasicContextKb.hasActor(dest)
                        if (existactor) {
                            try {
                                val actor = ActorBasicContextKb.getActor(dest)!!
//println("%%% ActorContextTcpServer  $name | handleConnection actor:${actor.name}  ${infoThreads()}")
                                if (inputmsg.isRequest()) {
                                    //set conn in the msg to the actor
                                    inputmsg.conn = conn
                                }
                                MsgUtil.sendMsg(inputmsg, actor)
                            } catch (e1: Exception) {
                                println("%%% ActorContextTcpServer $name |  ${e1.message}")
                            }
                        } else println("%%% ActorContextTcpServer $name | WARNING!! no local actor ${dest} in ${ActorBasicContextKb.name}")
                    }// msg != null
                    else{ msg == null
                        conn.closeConnection()
                        sysUtil.connActive.remove(conn)
                        break
                    }
                }
            } catch (e: Exception) {
                println("%%% ActorContextTcpServer $name | handleConnection: ${e.message}")
                sysUtil.connActive.remove(conn)
            }
        }//scope
    }//handleConnection

    override fun terminate(){
        sysUtil.connActive.forEach{ it.closeConnection() }
        //Dispatchers.IO.cancel()
        //sysUtil.userThreadContext.close()
        connInput.cancel()
        println("%%% ActorContextTcpServer $name | TERMINATE ... ")
        super.terminate()
    }

}

