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
 
 
 

class TestPlan0 {
	
	companion object{
		val channel       = Channel<String>()
		var systemStarted = false

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
 				while( QakContext.getActor("basicrobot") == null )		{
					println("waiting for system startup ...")
					delay(500)
				}
				channel.send("start")
			}
			//while( )
		}
	}
	
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
	
  
	@Test
	fun test0(){
 		println("test0 ........................")
		assertEquals( "", "")
	}

	@Test
	fun test1(){
 		println("test1 ........................")
		assertEquals( "", "")
	}
		
}