package it.unibo.actor0

import it.unibo.interaction.IJavaActor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import org.json.JSONObject

@kotlinx.coroutines.ObsoleteCoroutinesApi


enum class DispatchType {single, iobound, cpubound }

abstract class ActorBasicKotlin(val name: String,
                                val scope: CoroutineScope = GlobalScope,
                        val dispatchType: DispatchType = DispatchType.single,
                        val channelSize : Int = 50 ) : IJavaActor{
    constructor(name: String) : this(name, GlobalScope, DispatchType.single, 50) {

    }


    val tt      = "               %%% "
    private val actorobservers =  mutableListOf<IJavaActor>()
    protected var actorLogfileName  : String = ""
    protected var msgLogNoCtxDir   = "logs/noctx"
    protected var msgLogDir        = msgLogNoCtxDir
    var dispatcher : CoroutineDispatcher

    //var ctx  : ActorContextLocal? = null //to be injected

    init{                                    //Coap Jan2020
        //sysUtil.createMsglogFile("${name}_MsLog.txt")					//APR2020 : an Actor could have no context
        when( dispatchType ){
            DispatchType.single   -> dispatcher = sysUtil.singleThreadContext
            DispatchType.iobound  -> dispatcher = sysUtil.ioBoundThreadContext
            DispatchType.cpubound -> dispatcher = sysUtil.cpusThreadContext
        }
        //println("%%%  $name |  init  dispatcher=$dispatcher ${sysUtil.aboutThreads(name)}" )
        //ActorContextLocal.addActor( this )
        ActorContextNaive.addActor( this )
    }


    
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    val actor = scope.actor<ApplMessage>(dispatcher, capacity = channelSize) {
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
    abstract suspend protected  fun handleInput(msg : ApplMessage);

    open fun writeMsgLog( msg: ApplMessage){ //APR2020
        sysUtil.updateLogfile(actorLogfileName, "item($name,nostate,$msg).", dir = msgLogDir)
    }

    //------------------------------------------------
    //Utility ops to communicate with another, known actor
    suspend fun forward(msgId : String, msg: String, destActor: ActorBasicKotlin) {
        destActor.actor.send(
                MsgUtil.buildDispatch(name, msgId, msg, destActor.name))
    }

    suspend fun request(msgId : String, msg: String, destActor: ActorBasicKotlin) {
        val m = MsgUtil.buildRequest(name, msgId, msg, destActor.name)
        destActor.actor.send(m)
    }

    suspend fun reply(msgId : String, msg: String, destActor: ActorBasicKotlin) {
        val m = MsgUtil.buildReply(name, msgId, msg, destActor.name)
        destActor.actor.send(m)
    }

    fun showMsg(msg:String){ println("$name | $msg")  }
    fun aboutThreads() {  println( sysUtil.aboutThreads(name))  }

    //------------------------------------------------


    fun terminate() {
        actor.close()
    }


    //---------------------------------------------------------------------------
    //IJavaActor
    override fun myname() : String{ return name }
    override fun send(applMessageStr:  String ){    //JSON data from support
        //{"endmove":"true","move":"moveForward"}
        try{

            println( "$name | send $applMessageStr " )
            val msg = ApplMessage.create(applMessageStr )  //TODO problem with , (@)
            scope.launch{ actor.send( msg ) }
        }catch( e : Exception) {
            println("$name |  send $applMessageStr NOT ApplMessage")
        }
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
        scope.launch {   dest.actor.send(info)  }
    }

    fun sendToYourself( msg: ApplMessage ) {    //for standard Java => create new Thread
        //println("$name  sendToMyself  ${msg}" );
        //forward( msg.msgId, msg.msgContent, this )
        scope.launch {   actor.send(msg)  }
    }
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    
    suspend open fun autoMsg(  msg : ApplMessage) {
        //println("ActorBasic $name | autoMsg $msg actor=${actor}")
        actor.send( msg )
    }

    suspend protected  fun updateObservers(info: ApplMessage) {
        //println("$name  update  ${actorobservers.size}" );
        //actorobservers.forEach{ scope.launch {   it.actor.send(info)  } }
        actorobservers.forEach{   (it as ActorBasicKotlin).actor.send(info)   }
        //actorobservers.forEach{   println( "${it}" ); MsgUtil.sendMsg(info, it)   }
        //actorobservers.forEach{ println( "${it.name}" ) }
    }

    fun doupdateObservers(info: ApplMessage) {
        scope.launch{ actorobservers.forEach{   (it as ActorBasicKotlin).actor.send(info)   } }
    }


}