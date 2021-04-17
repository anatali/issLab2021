package it.unibo.actor0

import it.unibo.interaction.IJavaActor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi

enum class DispatchType {single, iobound, cpubound }
/*
Each actor could have its own scope
 */
abstract class ActorBasicKotlin(val name: String,
                val scope: CoroutineScope = CoroutineScope( newSingleThreadContext("single_$name") ) ,
                val dispatchType: DispatchType = DispatchType.single,
                val channelSize : Int = 50 ) : IJavaActor{
    val tt                         = "               %%% "
    private val actorobservers     =  mutableListOf<IJavaActor>()
    protected var actorLogfileName  : String = ""
    protected var msgLogNoCtxDir   = "logs/noctx"
    protected var msgLogDir        = msgLogNoCtxDir
    var dispatcher : CoroutineDispatcher
    protected val msgQueueStore    = mutableListOf<ApplMessage>()

    //var ctx  : ActorBasicContext? = null //to be injected

    init{                                    //Coap Jan2020
        //sysUtil.createMsglogFile("${name}_MsLog.txt")					//APR2020 : an Actor could have no context
        when( dispatchType ){
            DispatchType.single   -> dispatcher = sysUtil.singleThreadContext
            DispatchType.iobound  -> dispatcher = sysUtil.ioBoundThreadContext
            DispatchType.cpubound -> dispatcher = sysUtil.cpusThreadContext
        }
        //println("%%%  $name |  init  dispatcher=$dispatcher ${sysUtil.aboutThreads(name)}" )
        //ActorContextLocal.addActor( this )
        ActorBasicContextKb.addActor( this )
    }


    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    val kactor = scope.actor<ApplMessage>(dispatcher, capacity = channelSize) {
        //println("%%% $name |  ${infoThreads()} ACTIVATED  scope=$scope dispatcher=$dispatcher" )
        for (msg in channel) {
            //println("%%% $name |  receives $msg   ${sysUtil.aboutThreads(name)} scope=$scope" )
            sysUtil.traceprintln("$tt   $name |  msg= $msg ")
            if( msg.msgId == "stopTheActor") {  terminate() }
            else scope.launch{ handleInput(msg) }
            //updateObservers( msg  )   //called by specialized actor
            //writeMsgLog( msg )        //called by specialized actor
        }
    }

    //fun getActor() : SendChannel<ApplMessage> { return actor }
    abstract suspend protected  fun handleInput(msg : ApplMessage)

    open fun writeMsgLog( msg: ApplMessage){ //APR2020
        sysUtil.updateLogfile(actorLogfileName, "item($name,nostate,$msg).", dir = msgLogDir)
    }

    //------------------------------------------------
    //Utility ops to communicate with another, known actor
    suspend fun forward(msgId : String, msg: String, destActor: ActorBasicKotlin) {
        destActor.kactor.send(
                MsgUtil.buildDispatch(name, msgId, msg, destActor.name))
    }
    suspend fun forward( msg:ApplMessage, destActor: ActorBasicKotlin) {
        destActor.kactor.send(
            MsgUtil.buildDispatch(name, msg.msgId, msg.msgContent, msg.msgReceiver))
    }

    suspend fun request(msgId : String, msg: String, destActor: ActorBasicKotlin) {
        val m = MsgUtil.buildRequest(name, msgId, msg, destActor.name)
        destActor.kactor.send(m)
    }

    suspend fun reply(msgId : String, msg: String, destActor: ActorBasicKotlin) {
        val m = MsgUtil.buildReply(name, msgId, msg, destActor.name)
        destActor.kactor.send(m)
    }

    fun showMsg(msg:String){ println("$name | $msg")  }
    fun aboutThreads() {  println( sysUtil.aboutThreads(name))  }
    fun infoThreads() : String {  return sysUtil.aboutThreads(name)  }

    //------------------------------------------------


    open fun terminate() {
        println("$name | TERMINATES ${this.infoThreads()}")
        kactor.close()
    }


    //---------------------------------------------------------------------------
    //IJavaActor
    override fun myname() : String{ return name }
    //The following op is called by the support that sees the actor as a IJavaActor object
    override fun send(applMessageStr:  String ){    //JSON data from support
        //{"endmove":"true","move":"moveForward"}
        try{
            //println( "$name | ActorBasicKotlin send from obj to actor $applMessageStr " )
            val msg = ApplMessage.create(applMessageStr )  //TODO see trick problem with , (@)
            scope.launch{ kactor.send( msg ) }
        }catch( e : Exception) {
            println("$name |  send $applMessageStr NOT ApplMessage")
        }
    }
    fun sendToYourself( msg: ApplMessage ) {    //for oop
        //println("$name  sendToMyself  ${msg}" );
        //forward( msg.msgId, msg.msgContent, this )
        scope.launch {   kactor.send(msg)  }
    }
    fun send( msg: ApplMessage ) {
        scope.launch {   kactor.send(msg)  }
    }
    override fun registerActor(obs: IJavaActor) {
        actorobservers.add(obs)
    }

    override fun removeActor(obs: IJavaActor) {
        actorobservers.remove(obs)
    }

    fun registerActor(obs: ActorBasicKotlin) {
        actorobservers.add(obs)
    }

    fun removeActor(obs: ActorBasicKotlin) {
        actorobservers.remove(obs)
    }

    protected  fun sendToActor(info: ApplMessage,  dest: ActorBasicKotlin) {
        scope.launch {   dest.kactor.send(info)  }
    }


    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend open fun autoMsg(  msg : ApplMessage) {
        //println("ActorBasic $name | autoMsg $msg actor=${actor}")
        kactor.send( msg )
    }

    suspend protected  fun updateObservers(info: ApplMessage) {
        println("$name  updateObservers  ${actorobservers.size}" );
        //actorobservers.forEach{ scope.launch {   it.actor.send(info)  } }
        actorobservers.forEach{   (it as ActorBasicKotlin).kactor.send(info)   }
        //actorobservers.forEach{   println( "${it}" ); MsgUtil.sendMsg(info, it)   }
        //actorobservers.forEach{ println( "${it.name}" ) }
    }

    fun doupdateObservers(info: ApplMessage) {
        scope.launch{ actorobservers.forEach{   (it as ActorBasicKotlin).kactor.send(info)   } }
    }


}