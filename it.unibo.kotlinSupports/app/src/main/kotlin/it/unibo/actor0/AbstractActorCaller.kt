package it.unibo.actor0

import com.andreapivetta.kolor.Color
import it.unibo.actorAppl.RobotRestCaller
import it.unibo.actorAppl.RobotServiceCaller
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject


abstract class AbstractActorCaller(name: String,
 scope: CoroutineScope, val web:Boolean=true) : ActorBasicKotlin( name, scope ) {

    var rsc: RobotServiceCaller? = null
    var restCaller: RobotRestCaller? = null

    fun forward (msg: ApplMessage ) {
        //colorPrint("$name | AbstractActorCaller forward  msg:$msg  ", Color.GREEN )
        val  destActorName = msg.msgReceiver
        val dest           = ActorBasicContextKb.getActor(destActorName)
        if( dest != null ) sendToActor( msg,dest  )
        else {
            if (!web) {
                if (rsc == null) rsc = RobotServiceCaller("robotServiceCaller", this)
                //colorPrint("$name | forward to $rsc msg:$msg  ", Color.GREEN )
                rsc?.send(msg.toString())
            }else { //web
                val content = msg.msgContent  //
                val move    = JSONObject( content ).get("robotmove").toString()
                colorPrint("$name | move=$move", Color.GREEN )
                RobotRestCaller.doPostSimple("localhost:8081",move)
            }
        }
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

