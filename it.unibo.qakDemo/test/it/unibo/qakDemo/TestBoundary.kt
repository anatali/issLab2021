package it.unibo.qakDemo

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
import java.io.BufferedReader
import java.io.File
 
 
class TestBoundary {
		
	companion object{
		var testingObserver       : CoapObserverForTesting ? = null
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		var myactor               : ActorBasic? = null
		var counter               = 1
		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			GlobalScope.launch{
				it.unibo.ctxbundarywalker.main() //keep the control
			}
			
			GlobalScope.launch{
				myactor=QakContext.getActor("basicboundarywalker")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("basicboundarywalker")
				}				
				delay(1000)	//Give time to move lr
				channelSyncStart.send("starttesting")
			}	 
		}//init
		
		@JvmStatic
	    @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}//companion object
	
	
	fun readFile( fname: String ) : String{
        val bufferedReader: BufferedReader = File(fname).bufferedReader()    
        val inputString = bufferedReader.use { it.readText() }
        //println(inputString)
		return inputString
	}
	
	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver=$testingObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			 
		} 
		if( testingObserver == null) testingObserver = CoapObserverForTesting("obsboudary${counter++}")
  	}
	
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${testingObserver!!.name}")
		testingObserver!!.terminate()
		testingObserver = null
	}
    
 	
    @Test
    fun doboundary(){
		println("+++++++++ doboundary ")
  		val channelForObserver = Channel<String>()		
 		testingObserver!!.addObserver( channelForObserver,"mapdone" )
		
		runBlocking{ 
 		    var result  =  channelForObserver.receive()
			println("+++++++++  doboundary RESULT=$result ")
   		    //assertEquals( result,  "mapdone")
			val map = result.replace("mapdone-","")
			val refmap = readFile("roomBoundaryFinal.txt")
			assertEquals( map,  refmap)
			//delay(1000)
  		}		 
		
		  
	}
}