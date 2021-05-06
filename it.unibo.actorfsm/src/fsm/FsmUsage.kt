package fsm

import kotlinx.coroutines.runBlocking
import utils.Messages
import utils.AppMsg
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import utils.virtualRobotSupportApp
 

val terminate = AppMsg.buildDispatch("main","any","stopTheActor","br")
val msg       = AppMsg.buildDispatch("main","cmd","cmd(w)","br")




@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun runAnActor( scope: CoroutineScope ){
//	val startMsg  = AppMsg.buildDispatch("main","start","start","dummyactor")
	
	fsm.traceOn = true
	val actor= demoactor( scope )
	
	delay( 50 )  //give the time to start (elaborate the autoStartSysMsg)
 	Messages.forward( "main","info","0", actor  )
 	delay(1000)
 	Messages.forward( "main","end","1", actor  )
	(actor.fsmactor as Job).join()	//waits for termination  	
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main()=runBlocking{
	println("main STARTS")
	runAnActor( this )
	println("main ENDS")
}