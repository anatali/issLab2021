package fsm

import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope

val terminate = MsgUtil.buildDispatch("main","any","stopTheActor","br")
val msg       = MsgUtil.buildDispatch("main","cmd","cmd(w)","br")


@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun runAnActor( scope: CoroutineScope ){

	fsm.traceOn = false
	val actor= demoactor( scope )
	
	delay( 50 )  //give the time to start (elaborate the autoStartSysMsg)
	MsgUtil.forward( "main","info","0", actor  )
 	delay(1000)
	MsgUtil.forward( "main","end","1", actor  )
	(actor.fsmactor as Job).join()	//waits for termination  	
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main()=runBlocking{
	println("main STARTS")
	runAnActor( this )
	println("main ENDS")
}