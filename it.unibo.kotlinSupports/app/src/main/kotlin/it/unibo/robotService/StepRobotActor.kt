package it.unibo.robotService

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.MsgUtil
import it.unibo.supports.ActorMsgs
import it.unibo.supports.TimerActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class StepRobotActor(name: String, val ownerActor: ActorBasicKotlin, scope: CoroutineScope )
            : AbstractRobotActor( name, "wenv", scope ) {

    protected enum class State {
        start, moving, obstacle, end
    }

    protected lateinit var timer: TimerActor
    protected var curState        = State.start
    protected var plannedMoveTime = 0
    protected var backMsg         = ""
    protected var answer          = ""
    private var StartTime: Long   = 0L

    init {
        println( "$name | StepRobotActor init $support ${infoThreads()}")
    }


    protected fun fsmstep(move: String, arg: String) {
        println(name+ " | state=" + curState + " move=" + move + " arg=" + arg)
        when (curState) {
            State.start -> {
                    if (move == ApplMsgs.stepId) {
                        support.registerActor(this)
                        StartTime = this.currentTime
                        timer = TimerActor("t0", this, scope )
                        //timer.send(ActorMsgs.startTimerMsg.replace("TIME", arg))
                        val m = MsgUtil.buildDispatch(name,ActorMsgs.startTimerId,
                            ActorMsgs.startTimerMsg.replace("TIME", arg),"t0")
                        timer.send(m)
                        plannedMoveTime = arg.toInt()
                        val attemptStepMsg = "{\"robotmove\":\"moveForward\", \"time\": TIME}"
                            .replace("TIME", "" + (plannedMoveTime + 100))
                        support.forward(attemptStepMsg)
                        curState = State.moving
                    }
            }
            State.moving -> {
                val dtVal = this.getDuration(StartTime)
                val dt = "" + dtVal
                println(  "$name | moving .... dt=$dt move=$move ${infoThreads()}")
                if (move == ActorMsgs.endTimerId) {
                    answer = ApplMsgs.stepDoneMsg
                    support.forward(ApplMsgs.haltMsg)
                    curState = State.end
                } else if (move == "collision") {
                    support.forward(ApplMsgs.haltMsg)
                    timer.kill()
                    if( dtVal < 320 ) {
                        answer = ApplMsgs.stepFailMsg.replace("TIME", dt)
                        backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT", dt)
                        println("$name | answer=$answer backMsg=$backMsg")
                        curState = State.obstacle
                    }else{ //step almost done
                        answer = ApplMsgs.stepDoneMsg
                        backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT", "20")
                        println("$name | answer=$answer littlebackMsg=$backMsg")
                        support.forward(backMsg)
                        //curState = State.end
                        curState = State.obstacle

                    }
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
                    val m = MsgUtil.buildDispatch( name,"stepAnswer", answer,ownerActor.myname() )
                    ownerActor.send(m)

                } else if (move == "collision") { //the last step was ok but with a collision
                    println(name.toString() + " | collision ? answer=" + answer)
                    if (answer == ApplMsgs.stepDoneMsg) ownerActor.send(answer) else ownerActor.send(
                        ApplMsgs.stepFailMsg.replace("TIME", "10")
                    )
                } else {
                    println(name.toString() + " | FATAL error - curState = " + curState)
                }
                support.removeActor(this)
                curState = State.start

                //terminate()
            }
            //else -> { println(name.toString() + " | error - curState = " + curState) }
        }
    }

/*
======================================================================================
 */
    override fun msgDriven(infoJson: JSONObject) {
        if (! infoJson.has("sonarName")) println("$name StepRobotActor |  msgDriven:$infoJson")
        if (infoJson.has(ApplMsgs.stepId)) {
            //println( name + " |  msgJson:" + msgJson);
            val time: String = infoJson.getString(ApplMsgs.stepId)
            fsmstep(ApplMsgs.stepId, time)
        } else if (infoJson.has(ActorMsgs.endTimerId)) {
            fsmstep(ActorMsgs.endTimerId, "")
        } else if (infoJson.has("endmove")) {
            fsmstep(infoJson.getString("move"), infoJson.getString("endmove"))
        } else if (infoJson.has("collision")) {  //put as the last
            fsmstep("collision", "")
        }
    }




}