package kotlinCode

import kotlinx.coroutines.runBlocking
import it.unibo.actor0.sysUtil
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actorAppl.NaiveActorKotlinObserver
import it.unibo.robotService.ApplMsgs
import kotlinx.coroutines.delay
 

class demo( name: String) : ActorBasicKotlin(name ) {
	
	override suspend fun handleInput(msg: ApplMessage) {
        println("$name  | msgJson=$msg   ")
		val result = CallRestWithApacheHTTP.doPath("ws")
 		println("doPath asnwer=$result")
 		 /*
        var msgJsonStr = info.msgContent
        val msgJson = JSONObject(msgJsonStr)
        println("$name  | msgJson=$msgJson   " )*/
    }

}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
		
        //val obs = NaiveActorKotlinObserver("obs", this) 
        //obs.send( ApplMsgs.stepRobot_l("main").toString() )
		
		val appl = demo("appl")
		//appl.send(ApplMsgs.stepRobot_r("main").toString())
		
		appl.send(ApplMsgs.startAny("main").toString())
		
		delay(1000)
        println("ENDS runBlocking ${sysUtil.curThread()}")
        
		appl.terminate()
    }
    println("ENDS main ${sysUtil.curThread()}")
}

/*
Errors occurred during the build. java.lang.ArrayIndexOutOfBoundsException
 		*/