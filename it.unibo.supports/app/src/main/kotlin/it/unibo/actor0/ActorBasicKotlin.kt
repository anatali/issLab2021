package it.unibo.actor0

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi

enum class DispatchType {single, iobound, cpubound }

abstract class ActorBasicKotlin(val name: String,
                                val scope: CoroutineScope = GlobalScope,
                        val dispatchType: DispatchType = DispatchType.single,
                        //var discardMessages : Boolean = false,
                       //val confined :    Boolean = true,
                       //val ioBound :     Boolean = false,
                       val channelSize : Int = 50 ) {

    //protected val scope: CoroutineScope = GlobalScope

    //private val kactor = CoroutineScope( Dispatchers.Default ).actor
    val tt      = "               %%% "
    private val actorobservers =  mutableListOf<ActorBasicKotlin>()
    lateinit private   var logo             : String 	        //Coap Jan2020
    lateinit protected var ActorResourceRep : String 			//Coap Jan2020
    protected var actorLogfileName  : String = ""
    protected var msgLogNoCtxDir   = "logs/noctx"
    protected var msgLogDir        = msgLogNoCtxDir
    lateinit var dispatcher : CoroutineDispatcher // = newSingleThreadContext("aThread")
    //lateinit var  actor : SendChannel<ApplMessage>
    var ctx  : ActorContextLocal? = null //to be injected

    init{                                    //Coap Jan2020
        //createMsglogFile()					//APR2020 : an Actor could have no context
        //isObservable = true
        //logo    = "       ActorBasicKotlin(Resource) $name "
        //ActorResourceRep = "$logo | created  "
        when( dispatchType ){
            DispatchType.single   -> dispatcher = sysUtil.singleThreadContext
            DispatchType.iobound  -> dispatcher = sysUtil.ioBoundThreadContext
            DispatchType.cpubound -> dispatcher = sysUtil.cpusThreadContext
        }
        //println("%%%  $name |  init  dispatcher=$dispatcher ${sysUtil.aboutThreads(name)}" )
        ActorContextLocal.addActor( this )
    }


    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    val actor = scope.actor<ApplMessage>(dispatcher, capacity = channelSize) {
        //println("ActorBasicKotlin $name |  RUNNING IN $dispatcher"  )
        for (msg in channel) {
            //println("%%% $name |  receives $msg   ${sysUtil.aboutThreads(name)} scope=$scope" )
            sysUtil.traceprintln("$tt   $name |  msg= $msg ")
            //actorBody(msg)
            if( msg.msgId == "stopTheActor") {  terminate() }
            else handleInput(msg)
            //updateObservers( msg  )   //called by specialized actor
            //writeMsgLog( msg )        //called by specialized actor
        }
    }

    fun getLocalActor() : SendChannel<ApplMessage> {return actor}

    /* To be overridden by the application designer
    protected suspend fun actorBody(msg : ApplMessage){
        if( msg.msgId == "startTheActor"  ) return
        if( msg.msgId == "stopTheActor") {  terminate() }
        else handleInput(msg)
    }*/

    abstract protected suspend fun handleInput(msg : ApplMessage);


    open fun createMsglogFile(){
        actorLogfileName  = "${name}_MsLog.txt"
        sysUtil.createFile(actorLogfileName, dir = msgLogNoCtxDir)
    }
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

    fun showMsg(msg:String){
        println("$name | $msg")
    }
    fun aboutThreads() {
        println( sysUtil.aboutThreads(name))
    }

    //------------------------------------------------


    fun terminate() {
        actor.close()
    }


    //---------------------------------------------------------------------------
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
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend open fun autoMsg(  msg : ApplMessage) {
        //println("ActorBasic $name | autoMsg $msg actor=${actor}")
        actor.send( msg )
    }

    suspend protected  fun updateObservers(info: ApplMessage) {
        //println("$name  update  ${actorobservers.size}" );
        //actorobservers.forEach{ scope.launch {   it.actor.send(info)  } }
        actorobservers.forEach{   it.actor.send(info)   }
        //actorobservers.forEach{   println( "${it}" ); MsgUtil.sendMsg(info, it)   }
        //actorobservers.forEach{ println( "${it.name}" ) }
    }




}