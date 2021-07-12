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
					println("waiting for system startup ...")
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
			    println("checkSystemStarted resumed ----------------------- ")
			}			
		}
 	}
	
	
  
	//@Test
	fun testl(){
 		println("test1 ........................")
		//Send a command and look at the result
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "basicrobot",obsanswer,"moveactivated")
			MsgUtil.sendMsg("cmd","cmd(l)",myactor!!)
			result = obsanswer.receive()
			println("test1 RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals( result, "moveactivated(l)")
	}

	//@Test
	fun testr(){
 		println("test1 ........................")
		//Send a command and look at the result
		val cmd = MsgUtil.buildDispatch("tester", "cmd", "cmd(r)", "basicrobot")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsr", "basicrobot",obsanswer,"moveactivated")
			MsgUtil.sendMsg(cmd, myactor!!)
			result = obsanswer.receive()
			println("test1 RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals( result, "moveactivated(r)")
	}
	
	@Test
	fun testpNoobstacle(){
 		println("testp ........................")
		//Send a command and look at the result
		val request = MsgUtil.buildRequest("tester", "step", "step(500)", "basicrobot")
		var result  = ""
		runBlocking{
			val obsanswer = Channel<String>()
			CoapObserverForTesting.addObserver("obsp","basicrobot",obsanswer,"stepDone")
			MsgUtil.sendMsg(request, myactor!!)
			result = obsanswer.receive()
			println("testp RESULT=$result")
			CoapObserverForTesting.removeObserver()
		}
		assertEquals( result, "stepDone(500)")
	}
	
		
}