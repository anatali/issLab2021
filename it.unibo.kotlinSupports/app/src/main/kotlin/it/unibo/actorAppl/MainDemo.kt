package it.unibo.actorAppl

import it.unibo.actor0.MsgUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import it.unibo.supports.NaiveActorKotlinObserver

class MainDemo : AbstracMainForActor() {


    override fun mainJob(){
        println("MainDemo | mainJob")
        //val myscope = CoroutineScope(Dispatchers.IO)
        val a = NaiveActorKotlinObserver("",0,myscope)
        val b = BasicStepRobotActor("stepRobot", a, wenvAddr="localhost", scope=myscope)
        println("MainDemo | b= $b")
        val stepMsg = ApplMsgs.stepMsg.replace("TIME", "350")
        val m = MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, "stepRobot"  )
        b.registerActor(a);
        b.send( m )
        b.send( m )
    }
}

fun main() {
    MainDemo().startmain()
}
