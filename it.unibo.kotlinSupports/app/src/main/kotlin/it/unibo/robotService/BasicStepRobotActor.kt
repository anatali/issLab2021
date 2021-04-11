/*
============================================================
BasicStepRobotActor

Accept a  ApplMsgs.stepMsg to move ahead the robot for a given time.

Returns a ApplMsgs.stepDoneMsg if the move is done with success.
Returns a ApplMsgs.stepFailMsg with TIME=DT if the move is interrupted
by an obstacle after time DT. In this case it moves back the robot for time DT


The map is a singleton object, managed by mapUtil
============================================================
*/
package it.unibo.robotService

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.supports.ActorMsgs
import it.unibo.supports.TimerActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
 
@ExperimentalCoroutinesApi
class BasicStepRobotActor(name: String, val ownerActor: ActorBasicKotlin,
                         scope: CoroutineScope,  wenvAddr: String ="wenv" )
            : AbstractRobotActor( name, wenvAddr, scope ) {

    protected lateinit var timer: TimerActor
    protected var plannedMoveTime = 0
    protected var backMsg         = ""
    protected var answer          = ""
    private var StartTime: Long   = 0L


    protected suspend fun doStepMove(move: String, time: String){
        timer = TimerActor("t0", this, scope )
        val m = MsgUtil.buildDispatch(name,ActorMsgs.startTimerId,
                ActorMsgs.startTimerMsg.replace("TIME", time),"t0")
        val moveTime    = time.toInt()
        plannedMoveTime = moveTime - 70
        println("$name | TIMEEEEE  $plannedMoveTime / $moveTime ")
        val attemptStepMsg = "{\"robotmove\":\"moveForward\", \"time\": TIME}"
                .replace("TIME", "" + (plannedMoveTime))
        timer.send(m)
        StartTime = this.currentTime
        println("$name | SENDDDDD $attemptStepMsg")
        support.forward(attemptStepMsg)  //WARNING: possible conflict with BasicRobotActor
    }//doStepMove

    fun endStepOk(  ){
        println("$name | endStepOk   ")
        answer = ApplMsgs.stepDoneMsg
        val m = MsgUtil.buildDispatch( name,"stepAnswer", answer, ownerActor.myname() )
        ownerActor.send(m)
    }

    fun endStepKo( dtVal : String){
        timer.kill()
        println("$name | collision DTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT $dtVal")
        //backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT", dt)
        //println("$name | answer=$answer backMsg=$backMsg")
        //delay(350)  //back immediately after a collision is not allowed
        //support.forward(backMsg)
        //delay(350)  //back immediately after a collision is not allowed
        answer = ApplMsgs.stepFailMsg.replace("TIME", dtVal)
        val m  = MsgUtil.buildDispatch( name,"stepAnswer", answer,ownerActor.myname() )
        ownerActor.send(m)
    }



/*
======================================================================================
 */
override suspend fun handleInput(msg: ApplMessage) {
    //println(  "$name | AbstractRobotActor handleInput msg=$msg")
    val infoJsonStr = msg.msgContent.replace("@",",")
    val infoJson    = JSONObject(infoJsonStr)
        if (! infoJson.has("sonarName")) {
            println("$name BasicStepRobotActor |  handleInput:$infoJson")
        }
        if (infoJson.has(ApplMsgs.stepId)) {
            val time: String = infoJson.getString(ApplMsgs.stepId)
            doStepMove(ApplMsgs.stepId, time);
        }else if (infoJson.has(ActorMsgs.endTimerId)) {
            this.endStepOk();
        }else if (infoJson.has("collision")) {  //HYPOTHESIS: no movebale obstacle
            val dtVal = this.getDuration(StartTime)
            this.endStepKo(""+dtVal);
        }else if (infoJson.has("robotmove")){
            msgDriven(infoJson);
        }else if (infoJson.has("endmove")){

        }
    }

    override fun msgDriven(msgJson: JSONObject) {
        support.forward(msgJson.toString())
    }


}