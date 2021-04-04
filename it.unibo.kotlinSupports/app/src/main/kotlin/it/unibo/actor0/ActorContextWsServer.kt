package it.unibo.actor0
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.supports.IssWsHttpKotlinSupport
import kotlinx.coroutines.*

/*
Server su porta ws 8020
*/

class ActorContextWsServer(name:String, scope: CoroutineScope )
                                        : ActorBasicKotlin( name, scope) {
    protected var hostName: String? = null
    val workTime = 1000L * 6000 //100 min	
    protected lateinit var support: IssWsHttpKotlinSupport

    init {
        System.setProperty("inputTimeOut", workTime.toString() )

        //this.sendToYourself(MsgUtil.startDefaultMsg)
        /*
        scope.launch(Dispatchers.IO) {
            autoMsg( MsgUtil.startDefaultMsg )
        }*/
        this.sendToYourself(MsgUtil.startDefaultMsg)
    }


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun handleInput(msg: ApplMessage) {
        //println("%%% ActorContextServer $name | READY TO RECEIVE CONNS ")
        waitForConnection()
    }

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend protected fun waitForConnection() {
        //We could handle several connections
        //GlobalScope.launch(Dispatchers.IO) {
        scope.launch(sysUtil.userThreadContext) {        //BLOCKS THE SYSTEM if we use DispatchType.single
            try {
                val port = ActorBasicContextKb.portNum
                while (true) {/*
                    println("%%% ActorContextServer $name | WAIT $protocol-CONNECTION on $port ${infoThreads()}")
                    val conn = factoryProtocol!!.createServerProtocolSupport(port) //BLOCKS
                    sysUtil.connActive.add(conn)
                    handleConnection( conn )*/
                }
            } catch (e: Exception) {
                 println("      ActorContextServer $name | WARNING: ${e.message}")
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
                sysUtil.traceprintln("%%% ActorContextServer $name | NEWWWWWWWWWW conn:$conn")
                while (true) {
                    val msg = conn.receiveALine()       //BLOCKING
//println("%%% ActorContextServer  $name | msg:$msg in ${sysUtil.aboutThreads(name)}")
                    if( msg != null ) {
                        val inputmsg = ApplMessage.create(msg)
//println("%%% ActorContextServer  $name | inputmsg:$inputmsg  ")
						//sysUtil.updateLogfile( actorLogfileName, inputmsg.toString(), dir=msgLogNoCtxDir )
                        if (inputmsg.isEvent() ) {
                            //propagateEvent(inputmsg)
                            continue
                        }
                        val dest = inputmsg.msgReceiver
//println("%%% ActorContextServer  $name | dest:$dest  ")
                        val existactor = ActorBasicContextKb.hasActor(dest)
                        if (existactor) {
                            try {
                                val actor = ActorBasicContextKb.getActor(dest)!!
//println("%%% ActorContextServer  $name | actor:$actor  ")
                                if (inputmsg.isRequest()) { //Oct2019
                                    //set conn in the msg to the actor
                                    inputmsg.conn = conn
                                }
                                MsgUtil.sendMsg(inputmsg, actor)
                            } catch (e1: Exception) {
                                println("%%% ActorContextServer $name |  ${e1.message}")
                            }
                        } else println("%%% ActorContextServer $name | WARNING!! no local actor ${dest} in ${ActorBasicContextKb.name}")
                    }// msg != null
                    else{
                        conn.closeConnection()
                        sysUtil.connActive.remove(conn)
                        break
                    }
                }
            } catch (e: Exception) {
                println("%%% ActorContextServer $name | handleConnection: ${e.message}")
                sysUtil.connActive.remove(conn)
            }
        //}//scope
    }//handleConnection

/*
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend fun propagateEvent(event : ApplMessage){
         ctx.actorMap.forEach{
             //sysUtil.traceprintln("       ActorContextServer $name | in ${ctx.name} propag $event to ${it.key} in ${it.value.context.name}")
             val a = it.value
             try{
                 a.actor.send(event)
             }catch( e1 : Exception) {
                println("               %%% ActorContextServer $name | propagateEvent WARNING: ${e1.message}")
             }
         }
    }*/
}

