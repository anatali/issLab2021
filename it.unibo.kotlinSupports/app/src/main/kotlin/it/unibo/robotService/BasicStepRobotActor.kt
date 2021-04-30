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
import mapRoomKotlin.TripInfo
import org.json.JSONObject
 
@ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
class BasicStepRobotActor(name: String="stepRobot", val ownerActor: ActorBasicKotlin,
                         scope: CoroutineScope,  wenvAddr: String ="wenv" )
            : AbstractRobotActor( name, wenvAddr, scope ) {

    protected var timer: TimerActor? = null
    protected var plannedMoveTime = 0
    protected var backMsg         = ""
    protected var answer          = ""
    private var StartTime: Long   = 0L

    private var currentBasicMove  = "";
    private var doingStep         = false;
    //protected var moves         = TripInfo()  //NO: the BasicStepRobotActor hos no notion of map

init{
    colorPrint("BasicStepRobotActor synch CREATED")
}
    protected suspend fun doStepMove( time: String ){
        if( doingStep || currentBasicMove.length > 0 ){
            colorPrint("$name | !!! WARNING: step already in execution ... ", Color.RED)
            //We send an answer, but perhaps the caller does not handle it ...
            //Better to raise an exccption ???
            answer = ApplMsgs.stepFailMsg.replace("TIME", "0")
            val m  = MsgUtil.buildDispatch( name,"stepAnswer", answer, ownerActor.myname() )
            ownerActor.send(m)
            //IN ANY CASE, we DO NOT execute msgQueueStore.add(...) since the caller must change its pattern
            return
        }
        doingStep = true;
        timer = TimerActor("t0", this, scope )
        val m = MsgUtil.buildDispatch(name,ActorMsgs.startTimerId,
                ActorMsgs.startTimerMsg.replace("TIME", time),"t0")
        val moveTime    = time.toInt()
        plannedMoveTime = moveTime - 10  //'tune' the possible delta in timimg ???
        //println("$name | TIMEEEEE  $plannedMoveTime / $moveTime ")
        val attemptStepMsg = "{\"robotmove\":\"moveForward\", \"time\": TIME}"
                .replace("TIME", "" + (plannedMoveTime))
        timer?.send(m)
        StartTime = this.currentTime
        support.forward(attemptStepMsg)  //WARNING: possible conflict with BasicRobotActor
    }//doStepMove

    fun endStepOk(  ){
        doingStep = false;
        answer = ApplMsgs.stepDoneMsg
        val m = MsgUtil.buildDispatch( name,"stepAnswer", answer, ownerActor.myname() )
        ownerActor.send(m)
    }

    suspend fun endStepKo( dtVal : String){
            doingStep = false;
            timer?.kill()
            //println("$name | endStepKo DTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT $dtVal")
            backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT", dtVal)
            colorPrint("$name | backMsg=$backMsg  wenvAddr=$wenvAddr", Color.GREEN )
            delay(350)  //after a collision there is a forward move still running
            val backResult = support.sendHttp(backMsg, "$wenvAddr:8090/api/move")
            colorPrint("$name | backResult=$backResult", Color.GREEN )
            answer = ApplMsgs.stepFailMsg.replace("TIME", dtVal)
            val m  = MsgUtil.buildDispatch( name,"stepAnswer", answer,ownerActor.myname() )
           // delay(350)  //give time to end the sysback
            ownerActor.send(m)
    }

/*
======================================================================================
 */


override suspend fun handleInput(msg: ApplMessage) {
    //DO NOT PRINT (not alterate timing)
    //colorPrint(  "$name BasicStepRobotActor|  handleInput $msg currentBasicMove=$currentBasicMove")
    val infoJsonStr = msg.msgContent
    val infoJson    = JSONObject(infoJsonStr)
        if (infoJson.has("sonarName")) {
            val m = MsgUtil.buildDispatch( name,"sonarEvent", infoJsonStr, ownerActor.myname() )
            ownerActor.send(m)    //owner=ctxserver => update also the components connected via TCP
            updateObservers(m)
        }else if( infoJson.has("robotmove")  ) {
            val move = infoJson.getString("robotmove")
            val moveResult = support.sendHttp(infoJson.toString(), "$wenvAddr:8090/api/move")
            colorPrint("$name | moveResult=$moveResult", Color.GREEN )
            val m = MsgUtil.buildDispatch( name,"endmove", moveResult, ownerActor.myname() )
            ownerActor.send(m)
        }else if (infoJson.has(ApplMsgs.stepId) ) {
            if( currentBasicMove.length > 0 ) {  //Step requested before the end of a basic move
                colorPrint("$name BasicStepRobotActor |  $currentBasicMove running: store $msg", Color.LIGHT_MAGENTA)
                msgQueueStore.add(msg)
                return
            }
            val time: String = infoJson.getString(ApplMsgs.stepId)
            doStepMove( time );
        }else if (infoJson.has(ActorMsgs.endTimerId)) {
            endStepOk();
        }else if (infoJson.has("collision")) {      //Hypothesis: no movable obstacles
            var dtVal = this.getDuration(StartTime)
            if(doingStep){
                if( dtVal > 50 ) dtVal = dtVal - 30 //attempt to avoid a collision while doing a backStep
                colorPrint("$name BasicStepRobotActor | doingStep=$doingStep currentBasicMove=$currentBasicMove dtVal=$dtVal", Color.LIGHT_MAGENTA)
                endStepKo(""+(dtVal))
            }
        }
    }

    suspend fun lookAtmsgQueueStore(){
        if( msgQueueStore.size > 0 ){
            val next    = msgQueueStore.removeAt(0)
            val msgJson = JSONObject(next.msgContent)
            //this.waitUser("msgQueueStore2")
            colorPrint("$name BasicStepRobotActor |  RESUMES from msgQueueStore: ${msgJson}", Color.LIGHT_MAGENTA )
            if( msgJson.has("step")){
                val time: String = msgJson.getString(ApplMsgs.stepId)
                doStepMove( time );
            } else{
                //msgDriven( msgJson  )
                colorPrint("$name | robotmove:$msgJson  ")
                currentBasicMove = msgJson.getString("robotmove")
                //resetInfoAboutSysBack()   //sysBack synch
                support.forward(msgJson.toString())
            }
        }
    }

    override fun msgDriven(infoJson: JSONObject) {     }


}