/*
PathExecutor.kt

An unibo-actor that executes a given sequence of moves (todoPath)
of the 'stepRobot' and returns and answer to its ownerActor
 */
package features


import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import it.unibo.webspring.demo.ObserverForSendingAnswer
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
class PathExecutor (name: String, scope: CoroutineScope,
                    val robot      : BasicStepRobotActor,
                    val ownerActor : ActorBasicKotlin
                    )
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

    init{
         resetStateVars()
    }


    protected fun resetStateVars() {
        curState = State.start
        todoPath = ""
        println("$name PathExecutor STARTS todoPath=$todoPath")
    }

    protected suspend fun nextMove()   {
        if( todoPath.length == 0 ){
             endOk()
        } else {
            //moves.showMap()
            val firstMove = todoPath[0]
            todoPath = todoPath.substring(1)
            println("$name PathExecutor nextMove - firstMove=$firstMove todoPath=$todoPath")
            delay(250)  //avoid too fast moving
            if (firstMove == 'w') {
                robot.send( ApplMsgs.stepRobot_step("$name" ) )
                curState = State.moving
            }else if (firstMove == 'l' || firstMove == 'r')  {
                curState = State.turning
                println("---- curState=$curState todoPath=$todoPath")
                robot.send( ApplMsgs.stepRobot_turn("$name", ""+firstMove) )
            }else if (firstMove == 's')  {
                curState = State.moving
                println("---- curState=$curState todoPath=$todoPath")
                robot.send( ApplMsgs.stepRobot_s("$name" ) )
            }
        }
    }

    protected fun obstacleFound() {
        println("$name | END FAIL ---------------- ")
        /*
        moves.showMap()
        try {
            moves.setObstacle()
        } catch (e: Exception) { //wall
            println("$name | outside the map " + e.message)
        }
        moves.showMap()*/
        ownerActor.send(ApplMsgs.executorFailEnd(name, moves.getJourney()))
        moves.resetJourney()
        curState = State.continueJob
    }

    fun endOk(){
        println("$name | END OK ---------------- ")
        ownerActor.send(ApplMsgs.executorOkEnd(name,moves.getJourney(),ownerActor.name))
        //moves.showMap()
        moves.resetJourney()
        curState = State.continueJob
    }

    protected suspend fun fsm(move: String, endmove: String) {
        println("$name | fsm state= $curState move= $move endmove=$endmove todoPath=$todoPath")
        when (curState) {
            State.start -> {
                nextMove()  //modifies curState in moving or turning
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
                    moves.updateJourney(moveShort!!)
                    //moves.showMap()
                    nextMove()  //modifies curState in moving or turning
                } else println("$name | FATAL ERROR in turning ")
            } //turning
            State.moving -> {
                println("$name | moving ... $move")
                if (move == ApplMsgs.stepDoneId) {
                     moves.updateJourney("w")
                     nextMove()
                } else if (move == ApplMsgs.stepFailId) {
                    obstacleFound()
                } else if (endmove == "true") {
                    moves.updateJourney("s")
                    nextMove()
                }
             }
        }
    }

    override fun terminate(){
        //stepper.terminate()
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
        val path     = "lrwwswss" //"wwwlwwwwlwwwlwwwwl" wlwwwwwwrwrr   wlwwwllwwwrwll

        //Create a local robot with its own observer for doing this test
        val obsRobot = ObserverForSendingAnswer("obsanswer",  this,
            { sysUtil.colorPrint("move answer=$it", Color.BLUE) } )
        val robot    = BasicStepRobotActor("stepRobot",obsRobot, this, "localhost")
        //Create a PathExecutor with ots own observer
        val obs1     = ObserverForSendingAnswer("obspath",  this, { println("path answer=$it") } )
        val executor = PathExecutor("pathExec", this, robot, obs1)
        //Set the PathExecutor as the owner of the robot
        obsRobot.owner = executor

        val cmdStr   = ApplMsgs.executorstartMsg.replace("PATHTODO", path)
        val cmd      = MsgUtil.buildDispatch("main",ApplMsgs.executorStartId,cmdStr,"executor")
        println("main | $cmd")

        executor.send(cmd)
        /*
        delay(6000)
        //Reset the observer
        obsRobot.owner = null  //to execute the callback
        robot.send( ApplMsgs.stepRobot_l("main"))
        robot.send( ApplMsgs.stepRobot_r("main"))
        //send another command
         //
        //executor.send(cmd)
         */
        delay(1000)
        robot.terminate()
        executor.terminate()
        obsRobot.terminate()
        obs1.terminate()
        println("ENDS runBlocking ${sysUtil.curThread()}")
        System.exit(0)
    }//runBlocking
    println("ENDS main ${sysUtil.curThread()}")
}
