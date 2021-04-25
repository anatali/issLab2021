package it.unibo.webspring.demo

import com.andreapivetta.kolor.Color
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.sysUtil
import it.unibo.actorAppl.BasicStepRobotActorCaller
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor

import it.unibo.supports.NaiveActorKotlinObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.stereotype.Component


@Component
object RobotResource {
     lateinit var myscope     : CoroutineScope
     lateinit var obs         : NaiveActorKotlinObserver
     lateinit var robot       : BasicStepRobotActorCaller
     var configured           = false
    /*
    val myscope              = CoroutineScope(Dispatchers.Default)
    //TODO: define an observer that updates the HTML page
    val obs   = NaiveActorKotlinObserver("obs", 0, myscope)
    val robot = BasicStepRobotActorCaller("robot", myscope )
    */

    fun initRobotResource(local: Boolean=false){
        if( configured ){
            sysUtil.colorPrint("RobotResource | already configured localRobot=$local - ${sysUtil.curThread()} " )
            return
        }
        //Not already condfigured
         configured = true
         myscope              = CoroutineScope(Dispatchers.Default)
        //TODO: define an observer that updates the HTML page
         obs     = NaiveActorKotlinObserver("obs", 0, myscope)
         robot   = BasicStepRobotActorCaller("robot", myscope )
         if( local ){
            BasicStepRobotActor("stepRobot", ownerActor=obs, myscope, "localhost")
         }
         sysUtil.colorPrint("RobotResource | configured localRobot=$local - ${sysUtil.curThread()} " )
    }

    fun execMove( move : String ) {
        when (move) {
            "l" -> robot.send(ApplMsgs.stepRobot_l("spring"))
            "r" -> robot.send(ApplMsgs.stepRobot_r("spring"))
            "w" -> robot.send(ApplMsgs.stepRobot_w("spring"))
            "s" -> robot.send(ApplMsgs.stepRobot_s("spring"))
            "p" -> robot.send(ApplMsgs.stepRobot_step("spring", "350"))
        }
    }


}

/*
    fun initRobotLocal(){
         sysUtil.colorPrint("RobotResource | init ${sysUtil.curThread()} ", Color.GREEN)
        //obs   = NaiveActorKotlinObserver("obs", 0, myscope)
        //Create a local BasicStepRobotActor => no need for a TCP calls
        BasicStepRobotActor("stepRobot", ownerActor=obs, myscope, "localhost")
        //robot = BasicStepRobotActorCaller("robot", myscope )
    }

    fun initRobotCaller(){
        /*
        val fp = FactoryProtocol(null, "TCP", "hmi")
        val conn : IConnInteraction = fp.createClientProtocolSupport("localhost", 8010)
        sysUtil.colorPrint("hmi | init connected:$conn", Color.BLUE)
        */
        sysUtil.colorPrint("RobotResource | init ${sysUtil.curThread()} ", Color.GREEN)
        //obs   = NaiveActorKotlinObserver("obs", 0, myscope)
        //robot = BasicStepRobotActorCaller("robot", myscope )
        //val reader = ConnectionReader ("reader", conn )
        //reader.registerActor(owner)	//the answer received by the reader are redirected to the owner
        //reader.send( ApplMsgs.startAny(name))
    }

 */