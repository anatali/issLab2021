package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import utils.Messages
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mapRoomKotlin.mapUtil
import boundary.boundaryrobot
import fsm.Fsm

class TestPlanBoundary {
lateinit var robot : Fsm 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		println("%%%  testBoundary prepare the result map expected ")
  		println( mapRoomKotlin.mapUtil.refMapForTesting )
   		//TODO: activate the application: SEE boundaryTest
 		
	}

	@After
	fun terminate() {
		println("%%%  testBoundary terminate ")
	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun boudaryTest() {
		println("%%%  testBoundary activates the application")
 		//TODO: activate an user simulator
	    //or send to the robotboundary the command start
		runBlocking{
			robot = boundaryrobot("boundaryrobot", this)
			delay(3000)  //give to the robot the time to start
			Messages.forward("main", "start","ok",robot)
			robot.waitTermination()
		}
		println("%%%  testBoundary performs the final test after user end")		
		assertTrue( mapUtil.map.toString().equals( mapRoomKotlin.mapUtil.refMapForTesting ) )
	}
	
}//TestBoundary