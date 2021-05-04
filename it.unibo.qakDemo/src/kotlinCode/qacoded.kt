package kotlinCode
import it.unibo.kactor.*
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.runBlocking

import kotlinx.coroutines.delay
import it.unibo.robotService.ApplMsgs

 


class qacoded( name : String ) : ActorBasic( name ){
	
	val a = demo("a")
	
    init{ 
		println("	$name starts ") 
    }
    override suspend fun actorBody(msg : ApplMessage){
        println("	$name handles $msg ")
		emit("alarm", "alarm(fire)")
		val answer = CallRestWithApacheHTTP.doPath("rl")
		println("	$name answer=$answer ")
		a.send(ApplMsgs.startAny("main").toString())
		//
    }
}//qacoded


fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
		val appl  = qacoded("appl")
		val Json  = "{\"a\":\"b\"}"
		appl.forward("cmd", "cmd($Json)" ,"appl" )             
 		delay(1000)
        println("ENDS runBlocking ${sysUtil.curThread()}")
        
    }
    println("ENDS main ${sysUtil.curThread()}")
}
