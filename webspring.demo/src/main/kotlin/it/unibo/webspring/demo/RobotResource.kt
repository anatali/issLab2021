package it.unibo.webspring.demo

import it.unibo.actor0.sysUtil
import it.unibo.actorAppl.BasicStepRobotActorCaller
import it.unibo.actorAppl.NaiveActorKotlinObserver
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.stereotype.Component


@Component
object RobotResource {
     lateinit var myscope     : CoroutineScope
     lateinit var obs         : NaiveActorKotlinObserver
     lateinit var robot       : BasicStepRobotActorCaller
     var configured           = false


    fun initRobotResource(local: Boolean=false){
        if( configured ){
            sysUtil.colorPrint("RobotResource | already configured localRobot=$local - ${sysUtil.curThread()} " )
            return
        }
        //Not already condfigured
         configured = true
         myscope    = CoroutineScope(Dispatchers.Default)
         obs        = NaiveActorKotlinObserver("obs", myscope)
        //TODO: define an observer that updates the HTML page
         robot   = BasicStepRobotActorCaller("robot", myscope )
         if( local ){
            BasicStepRobotActor("stepRobot", ownerActor=obs, myscope, "wenv")
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
