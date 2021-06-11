package it.unibo.webspring.demo

import it.unibo.actor0.sysUtil
import it.unibo.actorAppl.BasicStepRobotActorCaller
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.stereotype.Component

@kotlinx.coroutines.ObsoleteCoroutinesApi
@Component
object RobotResource {
     lateinit var myscope     : CoroutineScope
     lateinit var obs         : DoNothingObserver
     lateinit var robot       : BasicStepRobotActor
     lateinit var robotCaller  : BasicStepRobotActorCaller
     var configured           = false

    fun initRobotResource(local: Boolean=true){
        if( configured ){
            sysUtil.colorPrint("RobotResource | already configured localRobot=$local - ${sysUtil.curThread()} " )
            return
        }
        //Not already configured
         configured = true
         myscope    = CoroutineScope(Dispatchers.Default)
         obs        = DoNothingObserver("obs", myscope)
        //TODO: define an observer that updates the HTML page
         robotCaller = BasicStepRobotActorCaller("robot", myscope )
         if( local ){
            robot = BasicStepRobotActor("stepRobot", ownerActor=obs, myscope, "localhost")
         }
         sysUtil.colorPrint("RobotResource | configured localRobot=$local - ${sysUtil.curThread()} " )
    }

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

}
