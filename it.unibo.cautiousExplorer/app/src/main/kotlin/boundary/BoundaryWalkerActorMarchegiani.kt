package boundary

import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mapRoomKotlin.TripInfo
import org.json.JSONObject

enum class State{
    START, WALKING, OBSTACLE, END
}

class BoundaryWalkerActorMarchegiani(name: String, scope: CoroutineScope)
    : ActorBasicKotlin(name, scope) {

    private val TIME_UNIT = "350"

    protected var state : State = State.START
    private var moves : TripInfo = TripInfo()
    private var bsra = BasicStepRobotActor("stepRobot", this, wenvAddr = "localhost", scope = scope)
    private var stepNum = 1;
    private var dimensions : MutableList<Double> = mutableListOf()

    private val stepMsg =
        MsgUtil.buildDispatch(name, ApplMsgs.stepId, ApplMsgs.stepMsg.replace("TIME", TIME_UNIT),
            "stepRobot")
    private val forwardMsg =
        MsgUtil.buildDispatch(name, ApplMsgs.robotMovecmdMsg,
            ApplMsgs.forwardMsg, "stepRobot")
    private val turnLeftMsg =
        MsgUtil.buildDispatch(name, ApplMsgs.robotMovecmdMsg, ApplMsgs.turnLeftMsg, "stepRobot")
    private val turnRightMsg =
        MsgUtil.buildDispatch(name, ApplMsgs.robotMovecmdMsg, ApplMsgs.turnRightMsg, "stepRobot")

    init {
        bsra.registerActor(this)
    }

    fun reset() {
        println("$name | FINAL MAP:")
        moves.showMap()
        stepNum = 1
        state = State.START
        moves = TripInfo()
    }

    private suspend fun fsm(x : String, y : String) {
        println("$name | fsm state=$state, stepNum=$stepNum, x=$x, y=$y")
        when(state) {
            State.START -> {
                moves.showMap()
                dimensions.clear()
                dimensions.add(0.0);
                doStep()
                state = State.WALKING
            }

            State.WALKING -> {
                if(x == "stepDone" && y =="ok") {
                    moves.updateMovesRep("w");
                    dimensions.set(stepNum-1, dimensions.get(stepNum - 1)+1)
                    doStep();
                } else if(x=="stepFail") {
                    dimensions.set(stepNum-1,
                        dimensions.get(stepNum - 1)+ y.toDouble()/TIME_UNIT.toDouble())
                    delay(500)
                    doFwd()
                } else if(x == "moveForward" && y == "false") {
                    moves.updateMovesRep("f")
                    state = State.OBSTACLE
                    turnLeft()
                }
            }

            State.OBSTACLE -> {
                if(x == "turnLeft" && y == "true") {
                    if(stepNum < 4) {
                        stepNum++
                        dimensions.add(0.0)
                        moves.updateMovesRep("l")
                        moves.showMap()
                        state = State.WALKING
                        doStep()
                    } else {
                        state = State.END
                        turnLeft()
                    }
                }
            }

            State.END -> {
                if(x.equals("turnLeft")) {
                    println("$name | BOUNDARY WALK END")
                    moves.showMap()

                    print("$name | DIMENSIONS (RU): ")
                    for(dim in dimensions)
                        print("$dim x ")
                    print("\b\b\n")

                    val boundary : Double = dimensions.stream().mapToDouble { d -> d }.sum()
                    println("$name | BOUNDARY_LENGTH = $boundary RU")
                    turnRight()
                } else {
                    stepNum = 1;
                    state = State.START
                    moves = TripInfo()

                    bsra.terminate()
                    this.terminate()
                }
            }

            else -> {
                println("$name | error - current state = $state")
            }
        }
    }

    protected fun doStep() {
        bsra.send(stepMsg)
    }

    protected fun doFwd() {
        bsra.send(forwardMsg)
    }

    protected fun turnLeft() {
        bsra.send(turnLeftMsg)
    }

    protected fun turnRight() {
        bsra.send(turnRightMsg)
    }

    override suspend fun handleInput(msg: ApplMessage) {
        //colorPrint("boundary | msg=$msg", Color.LIGHT_RED)
        if(msg == MsgUtil.startDefaultMsg)
            fsm("", "")
        else
            msgDriven(JSONObject(msg.msgContent))
    }

    protected suspend fun msgDriven(json : JSONObject) {
        when {
            json.has("stepDone") -> fsm("stepDone", json.getString("stepDone"))
            json.has("stepFail") -> fsm("stepFail", json.getString("stepFail"))
            json.has("endmove")  -> fsm(json.getString("move"), json.getString("endmove"))
            //else -> println("$name | ignored message, json=$json")
        }
    }
}

fun main() {

    runBlocking {
        val boundaryActor = BoundaryWalkerActorMarchegiani("boundary", this)
        boundaryActor.send(MsgUtil.startDefaultMsg)
    }
}