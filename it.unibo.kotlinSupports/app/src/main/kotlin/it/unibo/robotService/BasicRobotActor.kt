package it.unibo.robotService

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.supports.IssWsHttpKotlinSupport
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

    val turnRightMsg = "{\"robotmove\":\"turnRight\",   \"time\": 300}"
    val turnLeftMsg  = "{\"robotmove\":\"turnLeft\",    \"time\": 300}"

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
    if (msg.msgId == MsgUtil.startDefaultId){ /*
        delay(500)
        support.forward(turnRightMsg)
        delay(1000)
        support.forward(turnLeftMsg)
        delay(500)*/
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
            val answer = MsgUtil.buildDispatch(name,"sonarInfo",sonarData, sender)
            this.updateObservers(answer)
        }else if(msgJson.has("collision")) {
            val answer = MsgUtil.buildDispatch(
            name,"event", "collision" , sender)
            println( "$name | send answer:" + answer)
        }else if (msgJson.has("robotmove")) { //"{\"robotmove\":\"turnLeft\", \"time\": 300}"
            println( "$name | send msgJson:" + msgJson)
            support.forward(msgJson.toString())
        }else if (msgJson.has(ApplMsgs.endMoveId)){
            val endmove: String = msgJson.getString("move")
            val answer = MsgUtil.buildDispatch(name, ApplMsgs.endMoveId, endmove , sender)
            println( "$name | send answer:" + answer)
            this.updateObservers(answer)
        }
    }//else

    }//handleInput

    override fun msgDriven(infoJson: JSONObject) {
        TODO("Not yet implemented since we redefine handleInput")
    }
}

