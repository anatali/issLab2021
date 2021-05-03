/*
PathExecutor.kt

An unibo-actor that executes a given sequence of moves (todoPath)
of the 'stepRobot' and returns and answer to its ownerActor
The call to the 'stepRobot' is executed by using the BasicStepGenericCaller
-----------------------------------------------------------------
WARNING: if you use the virtualrobotandstepper SERVICE
DO NOT CREATE another BasicStepRobotActor
-----------------------------------------------------------------
 */
package demoRobotWithPlanner

import demoWithRobot.BasicStepGenericCaller
import demoWithRobot.NaiveObserver
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mapRoomKotlin.TripInfo
import org.json.JSONObject
import java.lang.Exception

/*
The map is a singleton object, managed by mapUtil
 */
@Suppress("REDUNDANT_ELSE_IN_WHEN")
class PathExecutor (name: String, scope: CoroutineScope, protected var ownerActor: ActorBasicKotlin)
    : ActorBasicKotlin( name, scope ) {
                                            //: AbstractRobotActor(name, "localhost") {
    protected enum class State { start, continueJob, moving, turning }

    protected val MoveNameShort = mutableMapOf<String, String>(
        "turnLeft" to "l",  "turnRight" to "r", "moveForward" to "w",
        "moveBackward" to "s" , "alarm" to "h"
    )

    protected var curState = State.start
    protected var todoPath = ""
    protected var moves    = TripInfo()

    protected var stepper : BasicStepGenericCaller
    protected lateinit var robot   : BasicStepRobotActor

    init{
         resetStateVars()
         stepper = BasicStepGenericCaller("gencaller", scope )//BasicStepRobotActor("stepper", this, scope, "localhost")
         //setup a receiver from TCP
         stepper.registerActor(this)
         //println("$name | STARTS ")
         //Uncomment if you want to use a local (non-TCP) BasicStepRobotActor
          //createStepRobotLocal(scope)
    }

    fun createStepRobotLocal(scope: CoroutineScope){
        //val obs = NaiveObserverActorKotlin("obs", scope)
        robot = BasicStepRobotActor("stepRobot",stepper, scope, "localhost")
    }

    protected fun resetStateVars() {
        curState = State.start
        todoPath = ""
        //moves    = TripInfo()
        println("$name PathExecutor STARTS todoPath=$todoPath")
    }

    protected suspend fun nextMove()   {
        if( todoPath.length == 0 ){
             endOk()
        } else {
            moves.showMap()
            val firstMove = todoPath[0]
            todoPath = todoPath.substring(1)
            println("$name PathExecutor nextMove - firstMove=$firstMove todoPath=$todoPath")
            delay(250)  //avoid too fast moving
            if (firstMove == 'w') {
                stepper.send( ApplMsgs.stepRobot_step("$name", "350") )
                curState = State.moving
            }else if (firstMove == 'l' || firstMove == 'r')  {
                curState = State.turning
                println("---- curState=$curState todoPath=$todoPath")
                stepper.send( ApplMsgs.stepRobot_turn("$name", ""+firstMove) )
            }
        }
    }

    protected fun obstacleFound() {
        println("$name | END FAIL ---------------- ")
        //moves.showMap()
        try {
            moves.setObstacle()
        } catch (e: Exception) { //wall
            println("$name | outside the map " + e.message)
        }
        moves.showMap()
        ownerActor.send(ApplMsgs.executorFailEnd(name, moves.getJourney()))
        moves.resetJourney()
        curState = State.continueJob
    }

    fun endOk(){
        println("$name | END OK ---------------- ")
        ownerActor.send(ApplMsgs.executorOkEnd(name))
        moves.showMap()
        moves.resetJourney()
        curState = State.continueJob
    }

    protected suspend fun fsm(move: String, endmove: String) {
        println("$name | fsm state= $curState move= $move endmove=$endmove todoPath=$todoPath")
        when (curState) {
            State.start -> {
                nextMove()  //modifies curState in moving or turning
                println("$name | fsm start - after move state= $curState")
           }
            State.continueJob -> {
                if (move == ApplMsgs.executorStartId) {
                    nextMove()  //modifies curState in moving or turning
                }
            }
            State.turning -> {
                println("$name | turning ... $move endmove= $endmove")
                val moveShort = MoveNameShort[move]
                if (endmove == "true") {
                    moves.updateMovesRep(moveShort!!)
                    moves.showMap()
                    nextMove()  //modifies curState in moving or turning
                } else println("$name | FATAL ERROR ")
            } //turning
            State.moving -> {
                println("$name | moving ... $move")
                if (move == ApplMsgs.stepDoneId) {
                     moves.updateMovesRep("w")
                     nextMove()
                } else if (move == ApplMsgs.stepFailId) {
                    obstacleFound()
                } else if (endmove == "true") {
                    nextMove()
                }
                println("$name | fsm moving - after move state= $curState")
            }
        }
    }

    override fun terminate(){
        stepper.terminate()
        //robot.terminate()
    }

    /*
======================================================================================
 */
    override suspend fun handleInput(msg: ApplMessage) {
        println("$name | handleInput ... $msg $curState")

        val msgJson = JSONObject( msg.msgContent )
        if (msgJson.has(ApplMsgs.executorStartId)) {
            //println("$name | handleInput  $msgJson")
            todoPath = msgJson.getString(ApplMsgs.executorStartId)
            fsm(ApplMsgs.executorStartId, "")
        } else if (msgJson.has(ApplMsgs.endMoveId)) {
            //println("$name | handleInput endMove:$msgJson $curState")
            val moveDone = msgJson.getString("move")
            fsm(moveDone, msgJson.getString(ApplMsgs.endMoveId))
        } else if (msgJson.has(ApplMsgs.stepDoneId)) {
            //println("$name | handleInput stepDone:$msgJson")
            fsm(ApplMsgs.stepDoneId, "")
        } else if (msgJson.has(ApplMsgs.stepFailId)) {
            //println("$name | handleInput stepFailed:$msgJson")
            fsm(ApplMsgs.stepFailId, msgJson.getString(ApplMsgs.stepFailId))
        }
    }


}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val importantPathToCheck = "wwlw"  //an obstacle with back that could collide
        val path     = importantPathToCheck //"wwwlwwwwlwwwlwwwwl" wlwwwwwwrwrr   wlwwwllwwwrwll
        val cmdStr   = ApplMsgs.executorstartMsg.replace("PATHTODO", path)
        val cmd      = MsgUtil.buildDispatch("main",ApplMsgs.executorStartId,cmdStr,"executor")
        println("main | $cmd")
        val obs      = NaiveObserver("peobs",  this)
        val executor = PathExecutor("pathExec", this, obs)
        delay(1000) //give time to connect with remote robot via TCP
        executor.send(cmd)
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
