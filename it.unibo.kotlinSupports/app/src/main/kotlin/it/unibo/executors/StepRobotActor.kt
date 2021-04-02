package it.unibo.executors

import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.interaction.IJavaActor
import it.unibo.supports.ActorMsgs
import it.unibo.supports.TimerActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.json.JSONObject

/*
==============================================================================
Accept a  ApplMsgs.stepMsg to move ahead the robot for a given time.
Returns a ApplMsgs.stepDoneMsg if the move is done with success.
Returns a ApplMsgs.stepFailMsg with TIME=DT if the move is interrupted
by an obstacle after time DT. In this case it moves back the robot for time DT
==============================================================================
 */


/*
The map is a singleton object, managed by mapUtil

 */
@ExperimentalCoroutinesApi
class StepRobotActor(name: String, val ownerActor: IJavaActor, scope: CoroutineScope) : AbstractRobotActor(name, scope) {
    protected enum class State {
        start, moving, obstacle, end
    }

    protected var curState = State.start
    //protected var ownerActor: IJavaActor? = null
    protected lateinit var timer: TimerActor
    protected var plannedMoveTime = 0
    protected var backMsg = ""
    protected var answer = ""
    private var StartTime: Long = 0

    //init {  this.ownerActor = ownerActor  }

    protected fun fsmstep(move: String, arg: String) {
        //println(name+ " | state=" + curState + " move=" + move + " arg=" + arg)
        when (curState) {
            State.start -> {
                //run {
                    if (move == ApplMsgs.stepId) {
                        StartTime = this.currentTime
                        timer = TimerActor("t0", this)
                        //timer.send(ActorMsgs.startTimerMsg.replace("TIME", arg))

                        //timer.send()
                        scope.launch {
                            MsgUtil.sendMsg(name, ActorMsgs.startTimerId,
                                ActorMsgs.startTimerMsg.replace("TIME", arg), timer)
                        }
                        plannedMoveTime = arg.toInt()
                        val attemptStepMsg = "{\"robotmove\":\"moveForward\", \"time\": TIME}"
                            .replace("TIME", "" + (plannedMoveTime + 100))
                        support.forward(attemptStepMsg)
                        curState = State.moving
                    }

            }
            State.moving -> {
                val dt = "" + this.getDuration(StartTime)
                println(  "$name | moving .... dt=$dt move=$move")
                if (move == ActorMsgs.endTimerId) {
                    answer = ApplMsgs.stepDoneMsg
                    support.forward(ApplMsgs.haltMsg)
                    curState = State.end
                } else if (move == "collision") {
                    support.forward(ApplMsgs.haltMsg)
                    timer.kill()
                    answer = ApplMsgs.stepFailMsg.replace("TIME", dt)
                    backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT", dt)
                    println(  "$name | answer=$answer backMsg=$backMsg")
                    curState = State.obstacle
                }
            } //moving
            State.obstacle -> {
                if (arg == "halted") {
                    println(name.toString() + " | obstacle  arg=" + arg)
                    support.forward(backMsg)
                    curState = State.end
                }
            }
            State.end -> {
                if (move == "moveBackward" && arg == "true" ||
                    move == "moveForward" && arg == "halted"
                ) {
                    //println(name.toString() + " | end  arg=" + arg)
                    //ownerActor.send(answer)
                    scope.launch {
                        MsgUtil.sendMsg(name, "stepAnswer", answer, ownerActor)
                    }


                } else if (move == "collision") { //the last step was ok but with a collision
                    println(name.toString() + " | collision ? answer=" + answer)
                    if (answer == ApplMsgs.stepDoneMsg) ownerActor.send(answer) else ownerActor.send(
                        ApplMsgs.stepFailMsg.replace(
                            "TIME",
                            "10"
                        )
                    )
                } else {
                    println(name.toString() + " | FATAL error - curState = " + curState)
                }
                support.removeActor(this)
                terminate()
            }
            else -> {
                println(name.toString() + " | error - curState = " + curState)
            }
        }
    }

/*
======================================================================================
 */
    override fun msgDriven(msgJson: JSONObject) {
        //if (! msgJson.has("sonarName")) println("$name StepRobotActor |  msgDriven:$msgJson")
        if (msgJson.has(ApplMsgs.stepId)) {
            //println( name + " |  msgJson:" + msgJson);
            val time: String = msgJson.getString(ApplMsgs.stepId)
            fsmstep(ApplMsgs.stepId, time)
        } else if (msgJson.has(ActorMsgs.endTimerId)) {
            fsmstep(ActorMsgs.endTimerId, "")
        } else if (msgJson.has("endmove")) {
            fsmstep(msgJson.getString("move"), msgJson.getString("endmove"))
        } else if (msgJson.has("collision")) {  //put as the last
            fsmstep("collision", "")
        }
    }




}