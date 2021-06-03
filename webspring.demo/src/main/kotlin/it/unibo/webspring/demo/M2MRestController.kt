package it.unibo.webspring.demo
import com.andreapivetta.kolor.Color
import features.PathExecutor
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@kotlinx.coroutines.ObsoleteCoroutinesApi
@RestController
class M2MRestController {
    lateinit var robot      : BasicStepRobotActor
    lateinit var obsRobot   : ObserverForSendingAnswerOnChannels
    lateinit var pathexec   : PathExecutor
    val myscope = CoroutineScope(Dispatchers.Default)
    var answerMove  = ""  //used by the ObserverForSendingAnswer
    var answerPath  = ""  //used by the ObserverForSendingAnswer
    var answerMoveChannel = Channel<String>()
    var answerPathChannel = Channel<String>()
    //Callback that leads to polling
    fun setAnswerForMove(v:String){ answerMove=v }  //used by ObserverForSendingAnswer
    fun setAnswerForPath(v:String){ answerPath=v }  //used by ObserverForSendingAnswer
    //
    //

    init{
        //RobotResource.initRobotResource() //OLD APPROACH: we want a local BasicStepRobotActorCaller
        //obsRobot    = ObserverForSendingAnswer("obsRobot", myscope, ::setAnswerForMove )
        obsRobot    = ObserverForSendingAnswerOnChannels("obsrobotch", myscope, answerMoveChannel)
        robot       = BasicStepRobotActor("stepRobot", ownerActor= obsRobot, myscope, "localhost")

        //The answer of the robot must go to the PathExecutor
        /*
        val obs1     = ObserverForSendingAnswer("obs1", myscope, { println("obs1 $it")  } )
        val robot1   = BasicStepRobotActor("stepRobot", ownerActor= obs1, myscope, "wenv")
        */
        //val obspath  = ObserverForSendingAnswer("obsPathanswer", myscope, ::setAnswerForPath )
        val obspath   = ObserverForSendingAnswerOnChannels("obspathch", myscope, answerPathChannel)
        pathexec      = PathExecutor("pathexec", myscope, robot, obspath)
        //obs1.owner   = pathexec
     }


    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @PostMapping( "/moverest" )
    fun doMove( @RequestParam(name="move", required=false, defaultValue="h") moveName : String) : String {
        sysUtil.colorPrint("M2MRestController | $moveName | $this")
        answerMove     = ""
        //RobotResource.execMove(robot, moveName)
        runBlocking{
            execMove(robot, moveName)
            /*
            while( answerMove=="" ){ //POLLING not so bad here, but better use channels
                //sysUtil.colorPrint("waiting for answer ...  ")
                delay(50)
            }*/
            answerMove=answerMoveChannel.receive()
        }//runBlocking
        sysUtil.colorPrint("result=$answerMove"  , Color.MAGENTA)
        return "$answerMove"
    }
    @PostMapping( "/dopath" )
    fun doPath( @RequestParam(name="path", required=false, defaultValue="") pathTodo : String) : String {
        sysUtil.colorPrint("M2MRestController | pathTodo=$pathTodo")
        //Activate PathExecutor and send the answer to the caller
        val cmdStr   = ApplMsgs.executorstartMsg.replace("PATHTODO", pathTodo)
        val cmd      = MsgUtil.buildDispatch("m2m",ApplMsgs.executorStartId,cmdStr,"pathexec")
        answerPath      = ""
        obsRobot.owner  = pathexec  //The answer of the robot must go to the PathExecutor
        runBlocking{
            pathexec.send(cmd)
            /*
            while( answerPath=="" ){ //POLLING not so bad here
                //sysUtil.colorPrint("waiting for answer ...  ")
                delay(50)
            }
            */
            answerPath=answerPathChannel.receive()
        }
        obsRobot.owner = null   //reset
        sysUtil.colorPrint("result=$answerPath"  , Color.MAGENTA)
        return "$answerPath"
    }



//--------------------------------------------------
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    fun execMove( robot: BasicStepRobotActor, move : String ) {
        sysUtil.colorPrint("execMove $move")
        when (move) {
            "l" -> robot.send(ApplMsgs.stepRobot_l("spring"))
            "r" -> robot.send(ApplMsgs.stepRobot_r("spring"))
            "w" -> robot.send(ApplMsgs.stepRobot_w("spring"))
            "s" -> robot.send(ApplMsgs.stepRobot_s("spring"))
            "p" -> robot.send(ApplMsgs.stepRobot_step("spring", "350"))
        }
    }
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    fun execMove(  move : String ) {
        execMove( robot, move )
    }
}//M2MRestController

//curl -d move=l localhost:8081/moverest

//https://code-with-me.jetbrains.com/lQaF6hxwe_Y-lf6az_zO2A#p=IC&fp=28C376F117E242763A56D8152AA07279F60FCAB06A9A2D80EDBFEF8C6737C385