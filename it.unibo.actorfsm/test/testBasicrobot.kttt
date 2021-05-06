package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.GlobalScope
import fsm.Fsm
import utils.Messages
import utils.AppMsg
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import utils.MqttUtils
import robotAppl.basicrobot
import robotAppl.sensorObserver
import robotAppl.basicrobotstate

class testBasicrobot {
	
lateinit var robot    : Fsm
val mqttTest   	      = MqttUtils("test")
val initDelayTime     = 4000L   // 
val useMqttInTest 	  = false
 
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		 //fsm.traceOn = true
 		kotlin.concurrent.thread(start = true) {	 
			robot = basicrobot("basicrobot", GlobalScope, usemqtt=useMqttInTest )
   			if( useMqttInTest ){
				 while( ! mqttTest.connectDone() ){
					  println( "	attempting MQTT-conn to ${fsm.mqttbrokerAddr}  for the test unit ... " )
					  Thread.sleep(1000)
					  mqttTest.connect("test_nat", fsm.mqttbrokerAddr )					 
				 }
				 sensorObserver("sensorobserver", GlobalScope, usemqtt=true )
 			}	
	} 
}	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("testBasicrobot terminated ")
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testObstacleLocal(){
		println(" --- testObstacleLocal ---")
 		runBlocking{
			delay(initDelayTime)  //time for robot to connect to mqttTest broker
 			Messages.forward( "test","cmd", "w", robot   )
 			delay(1800)
			//AFTER obstacle
  			assertTrue( basicrobot.rstate == basicrobotstate.obstacle)			
			Messages.forward( "test", "end", "end", robot   )
			println("testObstacleLocal END with robot in  ${basicrobot.rstate}")
			robot.waitTermination()
		}
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testObstacleRemote(){
		println(" --- testObstacleRemote ---")
 		runBlocking{
			delay(initDelayTime)  //time for robot to connect to mqttTest broker
			Messages.forward( "test","cmd", "w", robot.name, mqttTest   )
 			delay(1500)
			//AFTER obstacle
  			assertTrue( basicrobot.rstate == basicrobotstate.obstacle)			
			Messages.forward( "test", "end", "end", robot.name, mqttTest   )
			println("testObstacleRemote END with robot in  ${basicrobot.rstate}")
			robot.waitTermination()
		}
}
				
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testMovesLocal(){
		println(" --- testMovesLocal ---")
 		runBlocking{
			delay(initDelayTime)  //time for robot to connect to mqttTest broker
			Messages.forward( "test","cmd", "r", robot  )
			delay(500)
			assertTrue( basicrobot.rstate == basicrobotstate.rright)
			Messages.forward( "test","cmd", "l", robot  )
			delay(500)
			assertTrue( basicrobot.rstate == basicrobotstate.rleft)
			Messages.forward( "test","cmd", "w", robot  )
			delay(600)
 			assertTrue( basicrobot.rstate == basicrobotstate.forward)
			Messages.forward( "test","cmd", "h", robot  )
 			delay(1000)
 			assertTrue( basicrobot.rstate == basicrobotstate.stop)
			Messages.forward( "test","cmd", "s", robot  )
			delay(600)
			assertTrue( basicrobot.rstate == basicrobotstate.backward)
 			Messages.forward( "test","cmd", "h", robot  )
			delay(100)
 			assertTrue( basicrobot.rstate == basicrobotstate.stop)
 			Messages.forward( "test","end", "end", robot   )
			println("testLocal END with robot in  ${basicrobot.rstate}")
			robot.waitTermination()			

 		}
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testMovesRemote(){
		println(" --- testMovesRemote ---")
 		runBlocking{
			delay(initDelayTime)  //time for robot to connect to mqttTest broker
			Messages.forward( "test","cmd", "r", robot.name, mqttTest  )
			delay(500)
			assertTrue( basicrobot.rstate == basicrobotstate.rright)
			Messages.forward( "test","cmd", "l", robot.name, mqttTest  )
			delay(500)
			assertTrue( basicrobot.rstate == basicrobotstate.rleft)
			Messages.forward( "test","cmd", "w", robot.name, mqttTest  )
			delay(600)
 			assertTrue( basicrobot.rstate == basicrobotstate.forward)
			Messages.forward( "test","cmd", "h", robot.name, mqttTest  )
 			delay(1000)
 			assertTrue( basicrobot.rstate == basicrobotstate.stop)
			Messages.forward( "test","cmd", "s", robot.name, mqttTest  )
			delay(600)
			assertTrue( basicrobot.rstate == basicrobotstate.backward)
 			Messages.forward( "test","cmd", "h", robot.name, mqttTest  )
			delay(100)
 			assertTrue( basicrobot.rstate == basicrobotstate.stop)
 			Messages.forward( "test","end", "end", robot   )
			println("testLocal END with robot in  ${basicrobot.rstate}")
			robot.waitTermination()			
		}
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testBasicRobot(){
 			if( useMqttInTest ){
				testObstacleRemote()
 				//testMovesRemote()
				//testObstacleLocal() //still works  
			} 
			else{
				testMovesLocal()
				//testObstacleLocal()
			}		
			println("testBasicRobot BYE with robot in  ${basicrobot.rstate}")
	}

}