package it.unibo.qak21.basicrobot

import org.junit.Assert.*
import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
import org.junit.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import it.unibo.kactor.QakContext
import org.junit.Before
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.AfterClass
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ApplMessage
import org.junit.After
 
 
class TestPlan0 {
		
	companion object{
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		var testingObserver       : CoapObserverForTesting? = null
		var myactor               : ActorBasic? = null

		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			GlobalScope.launch{
				it.unibo.ctxbasicrobot.main() //keep the control
			}
			GlobalScope.launch{
				myactor=QakContext.getActor("basicrobot")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("basicrobot")
				}				
				//delay(1000)	//Give time to move lr
				channelSyncStart.send("starttesting")
				testingObserver = CoapObserverForTesting()
			}		 
		}//init
		
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}//companion object
	
	@Before
	fun checkSystemStarted()  {
	    println("+++++++++ checkSystemStarted ... ")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		}
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
		testingObserver!!.removeObserver()
	}
    
	//@Test
	fun testrotationmoves(){
 		println("+++++++++ testrotationmoves ")
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserver = Channel<String>()
			testingObserver!!.addObserver( channelForObserver!!,"moveactivated")
			
			MsgUtil.sendMsg("cmd","cmd(l)",myactor!!)
			result = channelForObserver!!.receive()
			println("+++++++++ testrotationmoves l RESULT=$result")
			assertEquals( result, "moveactivated(l)")
			
		    val cmd = MsgUtil.buildDispatch("tester", "cmd", "cmd(r)", "basicrobot")
			MsgUtil.sendMsg(cmd, myactor!!)
			result = channelForObserver!!.receive()
			println("+++++++++ testrotationmoves r RESULT=$result")
			assertEquals( result, "moveactivated(r)")			
		}	
	}
 	
	fun sendAndObserve( obschannel: Channel<String>, move: ApplMessage ) : String{
 		var result  = ""		
		val cmdh = MsgUtil.buildDispatch("tester", "cmd", "cmd(h)", "basicrobot")
		runBlocking{
 			MsgUtil.sendMsg(move, myactor!!)
			result = obschannel.receive()
			println("+++++++++  sendAndObserve RESULT=$result for move=$move")			
		    //The command w has the duration of 2500 msec and ALWAYS generates a collision ...
 			//if basicrobot enters in state handleObstacle, it executes s,h, but without updating 
		}
		return result
	}
	
	@Test 
	fun goAheadUntilObstacle()  {
		val channelForObserver = Channel<String>()
		sysUtil.waitUser("PLEASE, put the robot at HOME", 1000 )
		val cmdw = MsgUtil.buildDispatch("tester", "cmd", "cmd(w)", "basicrobot")
		testingObserver!!.addObserver( channelForObserver!!,"obstacle(w)" )		 
		var result  = sendAndObserve(channelForObserver!!,cmdw)
 		assertEquals( result, "obstacle(w)")
	}
	
 			
}