package demoWithRobot

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import it.unibo.actorAppl.RobotRestCaller
import it.unibo.robotService.ApplMsgs
import kotlinx.coroutines.runBlocking

object demoCallRest {
    val addr = "localhost:8081" ///moverest?move=$move" added by the caller
    fun doPath(path: String=""){
        RobotRestCaller.doPostBasicmove(addr,"r")
    }
}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        demoCallRest.doPath()
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
