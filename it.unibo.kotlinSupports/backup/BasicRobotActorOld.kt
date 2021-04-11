/*
============================================================
BasicRobotActor

Accept a  ApplMsgs.cmdMsg to move the robot
============================================================
*/
package it.unibo.robotService

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.json.JSONObject


@ExperimentalCoroutinesApi
class BasicRobotActorOld(name: String) : AbstractRobotActor( name ) {

    val turnRightMsg = "{\"robotmove\":\"turnRight\",\"time\":300}"
    val turnLeftMsg  = "{\"robotmove\":\"turnLeft\",\"time\":300}"
    private var engaged = false;
    private var working = false;

    init {
        println(  "$name | BasicRobotActor init support=$support")
    }

/*
======================================================================================
BasicRobotActor observes all the messages (events) sent by the WEnv
The WEnv emits events also when it is called by the StepRobotActor
Thus the BasicRobotActor updates its observers only when it is engaged

 */
    //Msg driven actor
    override suspend fun handleInput(msg: ApplMessage) {
    println(  "$name | BasicRobotActor handleInput msg=$msg")
    val sender  = msg.msgSender



    if (msg.msgId == "startbasicrobot"){ /*
        delay(500)
        //println( "$name | handleInput send $turnRightMsg"  )
        doMove('r')  //turnRight()
        //support.forward(turnRightMsg)
        delay(1000)
        support.forward(turnLeftMsg)
        delay(500) */
    }else if (msg.msgId == MsgUtil.endDefaultId){
        terminate()
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

    }//handleInput

    override fun msgDriven(infoJson: JSONObject) {
        TODO("Not yet implemented since we redefine handleInput")
    }
}

