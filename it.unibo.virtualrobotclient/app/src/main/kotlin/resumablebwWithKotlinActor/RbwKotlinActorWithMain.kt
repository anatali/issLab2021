package resumablebwWithKotlinActor


import it.unibo.actor0.ActorBasicKotlin
import it.unibo.supports2021.IssWsHttpJavaSupport
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.sysUtil
import it.unibo.consolegui.ConsoleGuiActor
import it.unibo.consolegui.ConsoleGuiActorKotlin
import it.unibo.interaction.IJavaActor
import it.unibo.robotWithActorJava.RobotMovesInfo
import it.unibo.supports.IssWsHttpKotlinSupport
import it.unibo.supports.WebSocketKotlinSupportUsage
import it.unibo.supports2021.ActorBasicJava
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class RbwKotlinActor(name:String, val support: IssWsHttpKotlinSupport) : ActorBasicKotlin(name), IJavaActor {
    val forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}"
    val backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 350}"
    val turnLeftMsg = "{\"robotmove\":\"turnLeft\", \"time\": 300}"
    val turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}"
    val haltMsg = "{\"robotmove\":\"alarm\", \"time\": 100}"

    private enum class State { start, walking, obstacle, end }

    private var curState = State.start
    private var stepNum = 1
    private val moves = RobotMovesInfo(true)
    private var tripStopped = true

    suspend protected fun fsm(move: String, endmove: String) {
        println(
            "$name | fsm state=$curState tripStopped=$tripStopped stepNum=$stepNum move=$move  endmove=$endmove"
        )
        when (curState) {
            State.start -> {
                //moves.cleanMovesRepresentation();
                moves.showRobotMovesRepresentation()
                if (move == "resume") {
                    curState = State.walking
                    doStep()
                    return
                }
                if (!tripStopped) {
                    doStep()
                    curState = State.walking
                } else {
                    println("please resume ...")
                }
            }
            State.walking -> {
                if (move == "resume") {
                    doStep()
                    return
                }
                if (move == "moveForward" && endmove == "true") {
                    //curState = State.walk;
                    moves.updateMovesRep("w")
                    if (!tripStopped) doStep() else {
                        println("please resume ...")
                    }
                } else if (move == "moveForward" && endmove == "false") {
                    //if (!tripStopped) {
                    curState = State.obstacle
                    turnLeft()
                    //}else System.out.println("please resume ...");
                } else {
                    println("IGNORE answer of turnLeft")
                }
            } //walk
            State.obstacle -> {
                if (move == "resume") {
                    curState = State.walking
                    doStep()
                    return
                }
                if (move == "turnLeft" && endmove == "true") {
                    if (stepNum < 4) {
                        stepNum++
                        moves.updateMovesRep("l")
                        moves.showRobotMovesRepresentation()
                        if (!tripStopped) {
                            curState = State.walking
                            doStep()
                        } else println("please resume ...")
                    } else {  //at home again
                        //if( ! tripStopped ) {
                        curState = State.end
                        turnLeft() //to force state transition
                        //}
                    }
                }
            }
            State.end -> {
                if (move == "turnLeft") {
                    println("BOUNDARY WALK END")
                    moves.showRobotMovesRepresentation()
                    turnRight() //to compensate last turnLeft
                } else {
                    println("RESET ... ")
                    stepNum = 1
                    curState = State.start
                    tripStopped = true
                    //moves           = new RobotMovesInfo(true);
                    moves.cleanMovesRepresentation()
                    moves.showRobotMovesRepresentation()
                }
            }
            else -> {
                println("error - curState = $curState")
            }
        }
    }

    override suspend fun handleInput(msg: ApplMessage) {     //called when a msg is in the queue
        println( "$name  | handleInput: $msg" )
        if (msg.msgId == "startApp") fsm("", "")
        else if (msg.msgId == "supportInfo") {//msg.msgContent is Json
            var msgJsonStr = msg.msgContent;
            println( "$name  | handleInput msgJsonStr: $msgJsonStr" )
            //HORRIBLE trick
            if( msgJsonStr.contains("@") ) msgJsonStr=msgJsonStr.replace("@",",")
             msgDriven( JSONObject(msgJsonStr) )
        }
        else if (msg.msgId == "robotcmd" )  //msg(robotcmd,dispatch,userConsole,any,resume,1)
            handleRobotCmd( msg.msgContent )
    }

    protected suspend fun msgDriven(infoJson: JSONObject) { //{"endmove":"true","move":"moveForward"}
        if (infoJson.has("endmove")) fsm(
            infoJson.getString("move"),
            infoJson.getString("endmove")
        ) else if (infoJson.has("sonarName")) handleSonar(infoJson)
          else if (infoJson.has("collision")) handleCollision(infoJson )
        //else if (infoJson.has("robotcmd")) handleRobotCmd(infoJson)

    }

    protected fun handleSonar(sonarinfo: JSONObject) {
        val sonarname = sonarinfo["sonarName"] as String
        val distance = sonarinfo["distance"] as Int
        //System.out.println("RobotApplication | handleSonar:" + sonarname + " distance=" + distance);
    }

    protected fun handleCollision(collisioninfo: JSONObject?) {
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotApplication | handleCollision move=" + move  );
    }

    protected suspend fun handleRobotCmd(cmd: String) {
        //val cmd = robotCmd["robotcmd"] as String
        println("====================================================")
        println("ResumableBoundaryWalkerActor | handleRobotCmd cmd=$cmd")
        println("====================================================")
        if (cmd == "stop") {
            tripStopped = true
        }
        if (cmd == "resume" && tripStopped) {
            tripStopped = false
            fsm("resume", "")
        }
    }

    //------------------------------------------------
    suspend protected  fun doStep() {
        delay(500) //to avoid too-rapid movement
        support.forward(forwardMsg)
    }

    suspend protected fun turnLeft() {
        delay(300) //to avoid too-rapid movement
        support.forward(turnLeftMsg)
    }

    suspend protected fun turnRight() {
        support.forward(turnRightMsg)
        delay(500) //to avoid too-rapid movement
    }
}

val doJob : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =
    fun(scope, support ) {

    val ra = RbwKotlinActor("rwa", support)
    support.registerActor(ra)

    val console = ConsoleGuiActorKotlin()
    console.registerActor(ra)
    //console.registerActor(new NaiveObserverActor("naiveObs") );

    //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
    val startAppMsg = ApplMessage("startApp", "dispatch", "main", ra.name, "go", "0" )
    ra.send(startAppMsg.toString())
    println("RbwKotlinActor | CREATED ${sysUtil.aboutSystem("doJob") }  ")
}

fun main(args: Array<String>) {
    val startTime = sysUtil.aboutSystem("applmain")
    sysUtil.trace = true
    println("==============================================")
    println("RbwKotlinActor | START ${sysUtil.aboutSystem("applmain") }"  );
    println("==============================================")
    runBlocking {
        val support = IssWsHttpKotlinSupport.createForWs(this, "localhost:8091")
        support.wsconnect(doJob)
            //System.out.println("MainRobotActorJava  | appl n_Threads=" + Thread.activeCount());

    }
    println("==============================================")
    println("RbwKotlinActor | main BEFORE END ${ActorBasicJava.aboutThreads()}" );
    println("==============================================")
    //support.close()
}