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
 
 
class TestPlan0 {
		
	companion object{
		val channel               = Channel<String>()
		var systemStarted         = false
		var myactor : ActorBasic? = null

		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			println("init")
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
				
				delay(3000)	//Give time to move
				channel.send("starttesting")
			}
		 
		}//init
		
	@JvmStatic
    @AfterClass
	fun terminate() {
		println("terminate")
	}
		
	}//object
	
	@Before
	fun checkSystemStarted()  {
		if( ! systemStarted ) {
			runBlocking{
				channel.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		}
 	}
	
	
  
	//@Test
	fun testl(){
 		println("+++++++++ testl ")
		//Send a command and look at the result
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "basicrobot",obsanswer,"moveactivated")
			MsgUtil.sendMsg("cmd","cmd(l)",myactor!!)
			result = obsanswer.receive()
			println("+++++++++ testl RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals( result, "moveactivated(l)")
	}

	//@Test
	fun testr(){
 		println("+++++++++  testr ")
		//Send a command and look at the result
		val cmd = MsgUtil.buildDispatch("tester", "cmd", "cmd(r)", "basicrobot")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "basicrobot",obsanswer,"moveactivated")
			MsgUtil.sendMsg(cmd, myactor!!)
			result = obsanswer.receive()
			println("+++++++++ testr RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals( result, "moveactivated(r)")
	}
	
	//@Test
	fun testpNoobstacle(){
		sysUtil.waitUser("PLEASE, put the robot at HOME" )  
 		println(" +++++++++ testpNoobstacle")
		//Send a command and look at the result
		val request = MsgUtil.buildRequest("tester", "step", "step(500)", "basicrobot")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsfortesting","basicrobot",obsanswer,"stepDone")
			MsgUtil.sendMsg(request, myactor!!)
			result = obsanswer.receive()
			println("+++++++++  testpNoobstacle RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals( result, "stepDone(500)")
	}
	
/*
 The usage of sysUtil.waitUser should be AVOIDED
 */
	//@Test
	fun testpWithobstacle(){
 		println(" +++++++++ testpWithobstacle")
		sysUtil.waitUser("PLEASE, put the robot near to an OBSTACLE", 10000)		
		//Send a command and look at the result
		val request = MsgUtil.buildRequest("tester", "step", "step(500)", "basicrobot")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsfortesting","basicrobot",obsanswer,"obstacle")
			MsgUtil.sendMsg(request, myactor!!)
			result = obsanswer.receive()
			println("+++++++++  testpWithobstacle RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals( result, "obstacle(unkknown)")
	}
	
	
	fun goAheadUntilObstacle(){
		//val request = MsgUtil.buildRequest("test", "step", "step(500)", "basicrobot")
		val cmdw = MsgUtil.buildDispatch("tester", "cmd", "cmd(w)", "basicrobot")
		//the command w has the duration of 1000 msec
		var result  = ""
		runBlocking{ 
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsgoahead","basicrobot",obsanswer )
			MsgUtil.sendMsg(cmdw, myactor!!)
			result = obsanswer.receive()
			while( ! result.contains("obstacle")   ){
				println("+++++++++  goAhead RESULT=$result")			
				delay(1500)
				MsgUtil.sendMsg(cmdw, myactor!!)
				result = obsanswer.receive()
			}
			CoapObserverForTesting.removeObserver()
			//assertEquals( result, "obstacle(unkknown)")
		}		
	}
	
	@Test
	fun testAlongWall(){
		sysUtil.waitUser("PLEASE, put the robot at HOME", 1000 )
		goAheadUntilObstacle()
	}
		
}