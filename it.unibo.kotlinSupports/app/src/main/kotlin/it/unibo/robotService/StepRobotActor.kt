/*
==============================================================================
StepRobotActor

Accept a  ApplMsgs.stepMsg to move ahead the robot for a given time.

Returns a ApplMsgs.stepDoneMsg if the move is done with success.
Returns a ApplMsgs.stepFailMsg with TIME=DT if the move is interrupted
by an obstacle after time DT. In this case it moves back the robot for time DT

The map is a singleton object, managed by mapUtil
==============================================================================
 */
package it.unibo.robotService

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
class StepRobotActor(name: String, val ownerActor: ActorBasicKotlin, scope: CoroutineScope )
            : AbstractRobotActor( name, "wenv", scope ) {

    protected enum class State { start, moving }

    protected lateinit var timer: TimerActor
    protected var curState        = State.start
    protected var plannedMoveTime = 0
    protected var backMsg         = ""
    protected var answer          = ""
    private var StartTime: Long   = 0L

    init {
        println( "$name | StepRobotActor init $support ${infoThreads()}")
    }


    protected suspend fun fsmstep(move: String, arg: String) {
        println(name+ " | state=" + curState + " move=" + move + " arg=" + arg)
        when (curState) {
            State.start -> {
                    if (move == ApplMsgs.stepId) {
                        support.registerActor(this)
                        timer = TimerActor("t0", this, scope )
                        //timer.send(ActorMsgs.startTimerMsg.replace("TIME", arg))
                        val m = MsgUtil.buildDispatch(name,ActorMsgs.startTimerId,
                            ActorMsgs.startTimerMsg.replace("TIME", arg),"t0")
                        val moveTime    = arg.toInt()
                        plannedMoveTime = moveTime - 70
                        println("$name | TIMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE $plannedMoveTime / $moveTime ")
                        val attemptStepMsg = "{\"robotmove\":\"moveForward\", \"time\": TIME}"
                            .replace("TIME", "" + (plannedMoveTime))
                        timer.send(m)
                        StartTime = this.currentTime
                        support.forward(attemptStepMsg)
                        curState = State.moving
                    }
            }
            State.moving -> {
                val dtVal = this.getDuration(StartTime)
                val dt = "" + dtVal
                if (move == ActorMsgs.endTimerId) {
                    println("$name | endtime  DTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT $dtVal")
                    answer = ApplMsgs.stepDoneMsg
                    //support.forward(ApplMsgs.haltMsg)       //to force state change
                    //curState = State.end
                    val m = MsgUtil.buildDispatch( name,"stepAnswer", answer,ownerActor.myname() )
                    ownerActor.send(m)
                    curState = State.start
                }else if (move == "collision") {
                    //support.forward(ApplMsgs.haltMsg)
                    timer.kill()
                    println("$name | collision DTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT $dtVal")
                    //backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT", dt)
                    //println("$name | answer=$answer backMsg=$backMsg")
                    //delay(350)  //back immediately after a collision is not allowed
                    //support.forward(backMsg)
                    //delay(350)  //back immediately after a collision is not allowed
                    answer = ApplMsgs.stepFailMsg.replace("TIME", dt)
                    val m  = MsgUtil.buildDispatch( name,"stepAnswer", answer,ownerActor.myname() )
                    ownerActor.send(m)
                    curState = State.start
                }
            } //moving
              //else -> { println(name.toString() + " | error - curState = " + curState) }
        }
    }

/*
======================================================================================
 */
override suspend fun handleInput(msg: ApplMessage) {
    //println(  "$name | AbstractRobotActor handleInput msg=$msg")
    val infoJsonStr = msg.msgContent //.replace("@",",")
    val infoJson    = JSONObject(infoJsonStr)
        if (! infoJson.has("sonarName")) println("$name StepRobotActor |  handleInput:$infoJson")
        if (infoJson.has(ApplMsgs.stepId)) {
            //println( name + " |  msgJson:" + msgJson);
            val time: String = infoJson.getString(ApplMsgs.stepId)
            fsmstep(ApplMsgs.stepId, time)
        } else if (infoJson.has(ActorMsgs.endTimerId)) {
            fsmstep(ActorMsgs.endTimerId, "")
        } /* else if (infoJson.has("endmove")) {
            fsmstep(infoJson.getString("move"), infoJson.getString("endmove"))
        } */ else if (infoJson.has("collision")) {  //put as the last
            fsmstep("collision", "")
        }
    }

    override fun msgDriven(infoJson: JSONObject) {
        //Already doe by suspend fun handleInput => also fsmstep is suspend
    }


}