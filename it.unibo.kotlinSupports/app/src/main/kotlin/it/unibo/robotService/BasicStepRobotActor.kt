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

import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.supports.ActorMsgs
import it.unibo.supports.TimerActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.json.JSONObject
 
@ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
class BasicStepRobotActor(name: String, val ownerActor: ActorBasicKotlin,
                         scope: CoroutineScope,  wenvAddr: String ="wenv" )
            : AbstractRobotActor( name, wenvAddr, scope ) {

    protected var timer: TimerActor? = null
    protected var plannedMoveTime = 0
    protected var backMsg         = ""
    protected var answer          = ""
    private var StartTime: Long   = 0L

    private var currentBasicMove  = "";
    private var stepGoingon       = false;

    protected suspend fun doStepMove( time: String ){
        if( stepGoingon || currentBasicMove.length > 0 ){
            //println("$name | STEPGOINGONNNNNNNNNNNNNNNNNNNN   ")
            answer = ApplMsgs.stepFailMsg.replace("TIME", "0")
            val m  = MsgUtil.buildDispatch( name,"stepAnswer", answer, ownerActor.myname() )
            ownerActor.send(m)
            return
        }
        stepGoingon = true;
        timer = TimerActor("t0", this, scope )
        val m = MsgUtil.buildDispatch(name,ActorMsgs.startTimerId,
                ActorMsgs.startTimerMsg.replace("TIME", time),"t0")
        val moveTime    = time.toInt()
        plannedMoveTime = moveTime - 70
        //println("$name | TIMEEEEE  $plannedMoveTime / $moveTime ")
        val attemptStepMsg = "{\"robotmove\":\"moveForward\", \"time\": TIME}"
                .replace("TIME", "" + (plannedMoveTime))
        timer?.send(m)
        StartTime = this.currentTime
        //println("$name | SENDDDDD $attemptStepMsg")
        support.forward(attemptStepMsg)  //WARNING: possible conflict with BasicRobotActor
    }//doStepMove

    fun endStepOk(  ){
        stepGoingon = false;
        //println("$name | endStepOk   ")
        answer = ApplMsgs.stepDoneMsg
        val m = MsgUtil.buildDispatch( name,"stepAnswer", answer, ownerActor.myname() )
        ownerActor.send(m)
    }

    suspend fun endStepKo( dtVal : String){
            stepGoingon = false;
            timer?.kill()
            //println("$name | endStepKo DTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT $dtVal")
            backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT", dtVal)
            colorPrint("$name | backMsg=$backMsg" )
            delay(350)  //back immediately after a collision is not allowed
            support.forward(backMsg)
            //no change to currentBasicMove => no result propagation
            //HYPOTHESIS: a little back move does not raise a collisio
            answer = ApplMsgs.stepFailMsg.replace("TIME", dtVal)
            val m  = MsgUtil.buildDispatch( name,"stepAnswer", answer,ownerActor.myname() )
            ownerActor.send(m)

    }



/*
======================================================================================
 */
override suspend fun handleInput(msg: ApplMessage) {
    colorPrint(  "$name | AbstractRobotActor handleInput $msg currentBasicMove=$currentBasicMove")
    val infoJsonStr = msg.msgContent //.replace("@",",")
    val infoJson    = JSONObject(infoJsonStr)
        //if (! infoJson.has("sonarName"))
            //println("$name BasicStepRobotActor |  handleInput:$infoJson")
        if (infoJson.has("sonarName")) {
            val m = MsgUtil.buildDispatch( name,"sonarEvent", infoJsonStr, ownerActor.myname() )
            ownerActor.send(m)    //update also the components connected via TCP
        }else if (infoJson.has(ApplMsgs.stepId) ) {
            if( currentBasicMove.length > 0 ) {
                colorPrint("$name BasicStepRobotActor |  $currentBasicMove running: store $msg", Color.LIGHT_MAGENTA)
                msgQueueStore.add(msg)
                return
            }
            val time: String = infoJson.getString(ApplMsgs.stepId)
            doStepMove( time );
        }else if (infoJson.has(ActorMsgs.endTimerId)) {
            endStepOk();
        }else if (infoJson.has("collision")) {      //Hypothesis: no movable obstacles
            if( currentBasicMove.length > 0  ){
                val payload = "{ \"collision\" : \"true\",\"move\": \"$currentBasicMove\"}"
                val m = MsgUtil.buildDispatch( name,"endmove", payload, ownerActor.myname() )
                ownerActor.send(m)
                //currentBasicMove = "";        //set by endMove
            }else{
                val dtVal = this.getDuration(StartTime)
                if( currentBasicMove.length == 0 ) this.endStepKo(""+dtVal)
                else{  //time elapsed for a convnetional move
                }
            }
        }else if( infoJson.has("robotmove") && currentBasicMove.length > 0 ){ //another move while working
            colorPrint("$name BasicStepRobotActor |  $currentBasicMove running: store $msg", Color.LIGHT_MAGENTA)
            msgQueueStore.add(msg)
            return
        }else if( infoJson.has("robotmove") && currentBasicMove.length == 0 ) {
            msgDriven(infoJson )
        }else if (infoJson.has("endmove")){
            val moveResult = infoJson.getString("endmove")
            if(  currentBasicMove.length > 0 ) {
                val payload = "{ \"endmove\" : \"$moveResult\",\"move\": \"$currentBasicMove\"}"
                val m = MsgUtil.buildDispatch( name,"endmove", payload, ownerActor.myname() )
                currentBasicMove = "";
                ownerActor.send(m)
            }else{
                val move = infoJson.getString("move")
                colorPrint("$name BasicStepRobotActor |  no currentBasicMove for ${move}" )
            }
            if( msgQueueStore.size > 0 ){
                val next    = msgQueueStore.removeAt(0)
                val msgJson = JSONObject(next.msgContent)
                //this.waitUser("msgQueueStore2")
                colorPrint("$name BasicStepRobotActor |  RESUMES from msgQueueStore: ${msgJson}", Color.LIGHT_MAGENTA )
                if( msgJson.has("step")){
                    val time: String = msgJson.getString(ApplMsgs.stepId)
                    doStepMove( time );
                } else msgDriven( msgJson  )
            }

        }
    }

    override fun msgDriven(infoJson: JSONObject) {
        //println("$name BasicStepRobotActor |  %%% msgDriven:$msgJson")
        /*
        if( msgJson.has("robotmove")){
            if( currentBasicMove.length > 0  ){
                println("$name BasicStepRobotActor |  move already running: let us store the request")
                msgQueueStore.add(msg)
                return
            }*/
        if( infoJson.has("robotmove")) {
            currentBasicMove = infoJson.getString("robotmove")
            support.forward(infoJson.toString())
        }
     }


}