package it.unibo.robotService

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.json.JSONObject

/*
==============================================================================
Accept a  ApplMsgs.cmdMsg to move the robot  .

==============================================================================
 */

@ExperimentalCoroutinesApi
class BasicRobotActor(name: String) : AbstractRobotActor( name ) {

    val turnRightMsg = "{\"robotmove\":\"turnRight\",\"time\":300}"
    val turnLeftMsg  = "{\"robotmove\":\"turnLeft\",\"time\":300}"

    init {
        println(  "$name | BasicRobotActor init support=$support")
    }

/*
======================================================================================
 */
    //Msg driven actor
    override suspend fun handleInput(msg: ApplMessage) {
    println(  "$name | BasicRobotActor handleInput msg=$msg")
    val sender  = msg.msgSender
    if (msg.msgId == "startbasicrobot"){
        delay(500)
        println( "$name | handleInput send $turnRightMsg"  )
        doMove('r')  //turnRight()
        //support.forward(turnRightMsg)
        delay(1000)
        support.forward(turnLeftMsg)
        delay(500)/* */
    }else if (msg.msgId == MsgUtil.endDefaultId){
        terminate()
    }else{
        val msgJson = JSONObject( msg.msgContent.replace("@",",") )  //HORRIBLE trick
        println( "$name | handleInput msgJson=" + msgJson)
        if ( msgJson.has("sonarName")){
            println("$name BasicRobotActor |  handleInput:$msgJson")
            //val answerStr = "msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )"
            val sonarName = msgJson.getString("sonarName")
            val distance  = msgJson.getInt("distance").toString()
            val sonarData = "${sonarName}_$distance"
            val payload   = "{'sonarInfo':'${sonarData}'}"
            val answer = MsgUtil.buildDispatch(name,"sonarInfo",payload, sender)
            this.updateObservers(answer)
        }else if(msgJson.has("collision")) {
            val payload   = "{'collision':'detected'}"
            val answer = MsgUtil.buildDispatch(name,"collision", payload, sender)
            println( "$name | send answer:" + answer)
            this.updateObservers(answer)
        }else if (msgJson.has("robotmove")) { //"{\"robotmove\":\"turnLeft\", \"time\": 300}"
            println( "$name | send msgJson:" + msgJson)
            support.forward(msgJson.toString())
        }else if (msgJson.has(ApplMsgs.endMoveId)){
            val moveres  = msgJson.getString(ApplMsgs.endMoveId)
            val movedone = msgJson.getString("move")
            val payload = "{\"${ApplMsgs.endMoveId}\":\"${moveres}\"@\"move\":\"$movedone\"}"
            val answer = MsgUtil.buildDispatch(name, ApplMsgs.endMoveId, payload , sender)
            println( "$name | send answer:" + answer)
            this.updateObservers(answer)
        }
    }//else

    }//handleInput

    override fun msgDriven(infoJson: JSONObject) {
        TODO("Not yet implemented since we redefine handleInput")
    }
}

