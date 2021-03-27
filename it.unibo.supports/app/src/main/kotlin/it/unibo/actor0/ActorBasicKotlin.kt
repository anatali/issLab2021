package it.unibo.actor0

import it.unibo.interaction.IJavaActor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import java.util.Vector
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Consumer
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
abstract class ActorBasicKotlin(val name: String,

                       var discardMessages : Boolean = false,
                       val confined :    Boolean = true,
                       val ioBound :     Boolean = false,
                       val channelSize : Int = 50 ) {

    val scope: CoroutineScope = GlobalScope

    //private val kactor = CoroutineScope( Dispatchers.Default ).actor
    val tt      = "               %%% "
    private val actorobservers =  mutableListOf<ActorBasicKotlin>()
    lateinit private   var logo             : String 	        //Coap Jan2020
    lateinit protected var ActorResourceRep : String 			//Coap Jan2020
    protected var actorLogfileName  : String = ""
    protected var msgLogNoCtxDir   = "logs/noctx"
    protected var msgLogDir        = msgLogNoCtxDir
    var dispatcher : CoroutineDispatcher = newSingleThreadContext("aThread")
/*
    constructor(myname: String) :
            this(myname,GlobalScope,false, true,false,50 ) {
        //configure()
        //dispatcher = sysUtil.singleThreadContext
            //if( confined ) sysUtil.singleThreadContext
            //else  if( ioBound ) sysUtil.ioBoundThreadContext
            //else sysUtil.cpusThreadContext
        println("ActorBasicKotlin $name |  CREATE  $myname confined=$confined dispatcher=$dispatcher" )
    }
*/
    init{                                    //Coap Jan2020
        createMsglogFile()					//APR2020 : an Actor could have no context
        //isObservable = true
        logo    = "       ActorBasicKotlin(Resource) $name "
        ActorResourceRep = "$logo | created  "
        dispatcher =
                if( confined ) sysUtil.singleThreadContext
                else  if( ioBound ) sysUtil.ioBoundThreadContext
                else sysUtil.cpusThreadContext
        //configure()
    }

    fun configure(){                                   //Coap Jan2020
         createMsglogFile()					//APR2020 : an Actor could have no context
        //isObservable = true
        logo    = "       ActorBasicKotlin(Resource) $name "
        ActorResourceRep = "$logo | created  "
        //dispatcher = sysUtil.singleThreadContext
            //if( confined ) sysUtil.singleThreadContext
            //else  if( ioBound ) sysUtil.ioBoundThreadContext
            //else sysUtil.cpusThreadContext
        println("******************* ActorBasicKotlin $name |  configure  confined=$confined dispatcher=$dispatcher" )
    }

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    val actor = scope.actor<ApplMessage>( dispatcher, capacity=channelSize ) {
        //println("ActorBasicKotlin $name |  RUNNING IN $dispatcher"  )
        for( msg in channel ) {
            //println("ActorBasicKotlin $name |  receives $msg"  )
            sysUtil.traceprintln("$tt ActorBasicKotlin  $name |  msg= $msg ")
            //updateObservers( msg  )   //called by specialized actor
            //writeMsgLog( msg )        //called by specialized actor
            if( msg.msgContent == "stopTheActor") {  channel.close() }
            else{
                actorBody( msg )
            }
        }
    }


    fun getLocalActor() : SendChannel<ApplMessage> {return actor}

    //To be overridden by the application designer
    abstract protected  fun actorBody(msg : ApplMessage)


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
    protected  fun sendToMyself( msg: ApplMessage ) {
        //println("$name  sendToMyself  ${msg}" );
        scope.launch {   getLocalActor().send(msg)  }
    }

    suspend protected  fun updateObservers(info: ApplMessage) {
        //println("$name  update  ${actorobservers.size}" );
        //actorobservers.forEach{ scope.launch {   it.actor.send(info)  } }
        actorobservers.forEach{   it.actor.send(info)   }
        //actorobservers.forEach{   println( "${it}" ); MsgUtil.sendMsg(info, it)   }
        //actorobservers.forEach{ println( "${it.name}" ) }
    }




}