/*
============================================================
BasicRobotActor

Accept a
    msg(engage,dispatch,SENDER,basicRobot,do,N)
    msg(robotmove,dispatch,SENDER,basicRobot,{"robotmove":"MOVE"@ "TIME": 300},1)

Observes
    all the messages (events) sent by the WEnv

Propagates
    the result of a move to the SENDER (connected TCP client)
    the observer events to the external observers (connected TCP clients)
============================================================
*/
package it.unibo.robotService

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.json.JSONObject


@ExperimentalCoroutinesApi
open class BasicRobotActor(name: String,  wenvAddr: String="wenv", scope: CoroutineScope )
        : AbstractRobotActor( name, wenvAddr, scope ) {

    val turnRightMsg = "{\"robotmove\":\"turnRight\",\"time\":300}"
    val turnLeftMsg  = "{\"robotmove\":\"turnLeft\",\"time\":300}"
    private var engagedBy = "";
    private var working   = false;

    init {
        println(  "$name | BasicRobotActor init support=$support")
    }

    override suspend fun handleInput(msg: ApplMessage) {
    println("$name | BasicRobotActor handleInput msg=$msg")
    //val sender = msg.msgSender
        when (msg.msgId) {
            "startbasicrobot" -> { }
            "engage"  -> engagedBy = msg.msgSender
            "release" -> engagedBy = ""
            MsgUtil.endDefaultId -> terminate()
            else -> { //move
                val msgJson = JSONObject(msg.msgContent.replace("@", ","))  //HORRIBLE trick
                println("$name | handleInput msgJson=" + msgJson)
                if( msgJson.has("robotmove") && working ){ //another move while still working
                    msgQueueStore.add(msg)
                } else elabRobotinfo(msgJson, msg.msgSender)
            }
        }
    }//handleInput

    suspend fun elabRobotinfo( infoJson: JSONObject, sender: String) {
        if (infoJson.has(ApplMsgs.robotMoveId)){
            if( engagedBy == sender || sender == "support" ) {
                msgDriven(infoJson)
            }else if( engagedBy != sender) {
                val move     = infoJson.getString(ApplMsgs.robotMoveId)
                val payload  = "{\"${ApplMsgs.endMoveId}\":\"notallowed_enaged\",\"move\":\"${move}\"}"
                //val answer   = MsgUtil.buildDispatch(name, ApplMsgs.endMoveId, payload, sender)
                val answer   = JSONObject(payload)
                sendMoveAnswer( answer, sender )
            }
        }else if (infoJson.has(ApplMsgs.endMoveId)){
            working = false;
            sendMoveAnswer( infoJson, sender )
            //this.waitUser("msgQueueStore1")
            if( msgQueueStore.size > 0 ){
                val next = msgQueueStore.removeAt(0)
                val msgJson = JSONObject(next.msgContent.replace("@", ","))
                //this.waitUser("msgQueueStore2")
                elabRobotinfo( msgJson,  next.msgSender )
            }
        }else if ( infoJson.has("sonarName")){
            val sonarName = infoJson.getString("sonarName")
            val distance  = infoJson.getInt("distance").toString()
            val sonarData = "${sonarName}_$distance"
            val payload   = "{'sonarInfo':'${sonarData}'}"
            val answer = MsgUtil.buildDispatch(name,"sonarInfo",payload, sender)
            this.updateObservers(answer)
        }

    }//elabRobotinfo


    override fun msgDriven(msgJson: JSONObject) { //execute a move
        println("$name | BasicRobotActor msgDriven $msgJson")
        if (msgJson.has("robotmove")) {
            if( ! working ) {
                support.forward(msgJson.toString())
                working = true;
            }
        }
    }

    suspend fun sendMoveAnswer( msgJson: JSONObject, sender: String ){
        val moveres  = msgJson.getString(ApplMsgs.endMoveId)
        val movedone = msgJson.getString("move")
        val payload  = "{\"${ApplMsgs.endMoveId}\":\"${moveres}\"@\"move\":\"$movedone\"}"
        val answer   = MsgUtil.buildDispatch(name, ApplMsgs.endMoveId, payload, sender)
        println("$name | send answer: $answer to $sender"   )
        updateObservers(answer) //a way to induce ActorContextTcpServer to send info to the sender
    }




/*
     }else{ //work
         val msgJson = JSONObject( msg.msgContent.replace("@",",") )  //HORRIBLE trick
         println( "$name | handleInput msgJson=" + msgJson)
         if (msgJson.has("robotmove")) {

         }else if (msgJson.has("robotmove")) { //"{\"robotmove\":\"turnLeft\", \"time\": 300}"
             //The BasicRobotActor is engaged
             if( ! engaged ) {
                 engaged = true
                 println("$name | send msgJson:$msgJson"   )
                 support.forward(msgJson.toString())
             }else{
                 println( "$name | WARNING: alrady engaged"  )
             }
        }else if (msgJson.has(ApplMsgs.endMoveId)) {
             val moveres  = msgJson.getString(ApplMsgs.endMoveId)
             val movedone = msgJson.getString("move")
             val payload  = "{\"${ApplMsgs.endMoveId}\":\"${moveres}\"@\"move\":\"$movedone\"}"
             val answer   = MsgUtil.buildDispatch(name, ApplMsgs.endMoveId, payload, sender)
             println("$name | send answer:" + answer)
             if (engaged) {
                 updateObservers(answer)
                 engaged = false
             }
        }else if ( msgJson.has("sonarName")){
            println("$name BasicRobotActor |  handleInput:$msgJson")
            //val answerStr = "msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )"
            val sonarName = msgJson.getString("sonarName")
            val distance  = msgJson.getInt("distance").toString()
            val sonarData = "${sonarName}_$distance"
            val payload   = "{'sonarInfo':'${sonarData}'}"
            val answer = MsgUtil.buildDispatch(name,"sonarInfo",payload, sender)
             if( engaged ) this.updateObservers(answer)
        }else if(msgJson.has("collision")) {
            val payload   = "{'collision':'detected'}"
            val answer = MsgUtil.buildDispatch(name,"collision", payload, sender)
            println( "$name | send answer:" + answer)
            if( engaged ) this.updateObservers(answer)
        }

    }//else work
*/



}

