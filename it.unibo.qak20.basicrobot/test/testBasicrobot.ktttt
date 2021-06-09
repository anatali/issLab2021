package test
 	//"tcp://mqtt.eclipse.org:1883"
	//mqtt.eclipse.org
	//tcp://test.mosquitto.org
	//mqtt.fluux.io
	//"tcp://broker.hivemq.com" 

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
 
 

class testBasicrobot {
	
var robot             : ActorBasic? = null
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 4000L   // 
val useMqttInTest 	  = false
val mqttbrokerAddr    = "tcp://broker.hivemq.com" 
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxbasicrobot.main() // MainCtxBasicrobot()
			println("testBasicrobot systemSetUp done")
			//robot = basicrobot("basicrobot", GlobalScope, usemqtt=useMqttInTest )
   			if( useMqttInTest ){
				 while( ! mqttTest.connectDone() ){
					  println( "	attempting MQTT-conn to ${mqttbrokerAddr}  for the test unit ... " )
					  Thread.sleep(1000)
					  mqttTest.connect("test_nat", mqttbrokerAddr )					 
				 }
////				 sensorObserver("sensorobserver", GlobalScope, usemqtt=true )
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
 			if( robot != null ){ MsgUtil.sendMsg( "test","cmd", "w", robot!!   )}
 			delay(1800)
			//AFTER obstacle
  			//assertTrue( basicrobot.rstate == basicrobotstate.obstacle)			
			if( robot != null ){ MsgUtil.sendMsg( "test", "end", "end", robot!!   ) }
			println("testObstacleLocal END with robot in  ") //${basicrobot.rstate}
			//if( robot != null ) robot!!.waitTermination()
		}
}


				
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testMovesLocal(){
		println(" --- testMovesLocal ---")
 		runBlocking{
			delay(initDelayTime)  //time for robot to start
			robot = it.unibo.kactor.sysUtil.getActor("basicrobot")
			if( robot != null )  MsgUtil.sendMsg( "test","cmd", "cmd(r)", robot!!  )
			delay(500)
			//assertTrue( robot!!.Rstate == basicrobotstate.rright)
			if( robot != null )  MsgUtil.sendMsg( "test","cmd", "cmd(l)", robot!!  )
			delay(500)
			//assertTrue( basicrobot.rstate == basicrobotstate.rleft)
			if( robot != null )  MsgUtil.sendMsg( "test","cmd", "cmd(w)", robot!!  )
			delay(600)
 			//assertTrue( basicrobot.rstate == basicrobotstate.forward)
			if( robot != null )  MsgUtil.sendMsg( "test","cmd", "cmd(h)", robot!!  )
 			delay(1000)
 			//assertTrue( basicrobot.rstate == basicrobotstate.stop)
			if( robot != null )  MsgUtil.sendMsg( "test","cmd", "cmd(s)", robot !! )
			delay(600)
			//assertTrue( basicrobot.rstate == basicrobotstate.backward)
 			if( robot != null )  MsgUtil.sendMsg( "test","cmd", "cmd(h)", robot!!  )
			delay(100)
 			//assertTrue( basicrobot.rstate == basicrobotstate.stop)
 			if( robot != null )  MsgUtil.sendMsg( "test","end", "end", robot!!   )
			println("testLocal END with robot in  ") //${basicrobot.rstate}
//			if( robot != null ) robot!!.waitTermination()			
 		}
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testBasicRobot(){
//			Thread.sleep(3000)
//			robot = it.unibo.kactor.sysUtil.getActor("basicrobot")
			//println("testBasicRobot starts with robot =$robot ")
 			if( useMqttInTest ){
				//testObstacleRemote()
 				//testMovesRemote()
				//testObstacleLocal() //still works  
			} 
			else{
				testMovesLocal()
				//testObstacleLocal()
			}		
			println("testBasicRobot BYE with robot in  ") //${basicrobot.rstate}
	}

}