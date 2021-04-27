package it.unibo.actor0

import com.andreapivetta.kolor.Color
import it.unibo.actorAppl.RobotServiceCaller
import kotlinx.coroutines.CoroutineScope


abstract class AbstractActorCaller(name: String, scope: CoroutineScope) //, val owner: IUniboActor
                         : ActorBasicKotlin( name, scope ) {

    var rsc: RobotServiceCaller? = null

    fun forward (msg: ApplMessage ) {
        //colorPrint("$name | AbstractActorCaller forward  msg:$msg  ", Color.GREEN )
        val  destActorName = msg.msgReceiver
        val dest           = ActorBasicContextKb.getActor(destActorName)
        if( dest != null ) sendToActor( msg,dest  )
        else{
            if( rsc == null ) rsc = RobotServiceCaller("robotServiceCaller", this)
            //colorPrint("$name | forward to $rsc msg:$msg  ", Color.GREEN )
            rsc?.send(msg.toString()) }
    }

    suspend fun forward(msgId : String, msg: String, destActorName: String) {
        val dest = ActorBasicContextKb.getActor(destActorName)
        if( dest != null ) forward(msgId, msg, dest)
        else{
            //TODO: find info about the remote destActorName
            val applMsg = "msg($msgId,dispatch,$name,DEST,CMD,1)"
                .replace("DEST", destActorName)
                .replace("CMD", msg)
            colorPrint("$name | AbstractActorCaller forward to rsc applMsg:$applMsg", Color.GREEN )
            //val msg = ApplMessage.create(applMsg)
            rsc?.send(applMsg)
        }
    }
 }

