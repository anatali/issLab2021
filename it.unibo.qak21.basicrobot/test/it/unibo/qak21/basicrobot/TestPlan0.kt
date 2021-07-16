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
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		}
		runBlocking{ delay(300) } //Put some interval between tests
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
		testingObserver!!.removeObserver()
	}
    
	@Test
	fun testrotationmoves(){
 		println("+++++++++ testrotationmoves ")
		//Send a command and look at the result
		var result  = ""
		runBlocking{
 			val channelForObserverL = Channel<String>()
			testingObserver!!.addObserver( channelForObserverL,"moveactivated(l)")
			
			MsgUtil.sendMsg("cmd","cmd(l)",myactor!!)
			result = channelForObserverL.receive()
			println("+++++++++ testrotationmoves l RESULT=$result")
			assertEquals( result, "moveactivated(l)")
			testingObserver!!.removeObserver()
			delay(200)			//
			
			val channelForObserverR = Channel<String>()
			testingObserver!!.addObserver( channelForObserverR,"moveactivated(r)")
		    val cmd = MsgUtil.buildDispatch("tester", "cmd", "cmd(r)", "basicrobot")
			MsgUtil.sendMsg(cmd, myactor!!)
			result = channelForObserverR.receive()
			println("+++++++++ testrotationmoves r RESULT=$result")
			delay(200)			//
			assertEquals( result, "moveactivated(r)")
		}	
	}
 
	@Test 
	fun goAheadUntilObstacle()  {
 		println("+++++++++ goAheadUntilObstacle ")
		val channelForObserver = Channel<String>()
		sysUtil.waitUser("PLEASE, put the robot at HOME", 1000 )
		val cmdw = MsgUtil.buildDispatch("tester", "cmd", "cmd(w)", "basicrobot")
		val cmdh = MsgUtil.buildDispatch("tester", "cmd", "cmd(h)", "basicrobot")
		
		testingObserver!!.addObserver( channelForObserver,"obstacle(w)" )
		
		runBlocking{
 		    var result  = ""		
 			MsgUtil.sendMsg(cmdw, myactor!!)
			result = channelForObserver.receive()
			println("+++++++++  sendAndObserve RESULT=$result for cmd=$cmdw")			
		    //The command w has the duration of 2500 msec and ALWAYS generates a collision ...
		    MsgUtil.sendMsg(cmdh, myactor!!)
 			//if basicrobot enters in state handleObstacle, it executes s,h, but without updating 
			delay( 500 ) //give time to compensate before closing the test
 		    assertEquals( result, "obstacle(w)")
		}		 
	}
	
    @Test
    fun dummy(){
		println("+++++++++ dummy ")
		 assertEquals( "1", "1")
	}
}