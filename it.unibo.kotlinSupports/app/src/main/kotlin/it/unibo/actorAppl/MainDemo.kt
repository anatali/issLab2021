package it.unibo.actorAppl

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import it.unibo.actor0.MsgUtil
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import it.unibo.supports.NaiveActorKotlinObserver
import kotlinx.coroutines.CoroutineScope

class MainDemo : AbstracMainForActor() {


    override  fun mainJob(scope: CoroutineScope) {
        colorPrint("MainDemo | mainJob", Color.CYAN)
        //val myscope = CoroutineScope(Dispatchers.IO)
        val a = NaiveActorKotlinObserver("",0,scope)
        val b = BasicStepRobotActor("stepRobot", a, wenvAddr="localhost", scope=scope)
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
