package virtualRobotUsage
//robotActorTry.kt

//import kotlinx.coroutines.runBlocking
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.ApplMessage
import it.unibo.actor0robot.ApplMsgs
import it.unibo.supports.FactoryProtocol
import it.unibo.supports2021.ActorBasicJava
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.Channel
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

//Actor that includes the business logic; the behavior is message-driven 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi 
val robotActorTry  : SendChannel<String>	=
	CoroutineScope( Dispatchers.Default ).actor {
	var state        = "working"
    val MoveJsonCmd  = HashMap<String, String>()
    lateinit var conn : IConnInteraction

	fun doInit() {
		try {
			MoveJsonCmd.put("w", ApplMsgs.forwardMsg.replace(",", "@"))
			MoveJsonCmd.put("s", ApplMsgs.backwardMsg.replace(",", "@"))
			MoveJsonCmd.put("l", ApplMsgs.turnLeftMsg.replace(",", "@"))
			MoveJsonCmd.put("r", ApplMsgs.turnRightMsg.replace(",", "@"))
			MoveJsonCmd.put("h", ApplMsgs.haltMsg.replace(",", "@"))

			val fp = FactoryProtocol(null, "TCP", "walker") //MsgUtil.getFactoryProtocol(Protocol.TCP);
			println("    ---  AbstractRobotRemote | fp:$fp")
			conn = fp.createClientProtocolSupport("localhost", 8010)
			println("    --- AbstractRobotRemote | connected:$conn")
			//reader = ConnectionReader("reader", conn)
			//reader.registerActor(this)
			//reader.send(it.unibo.remoteCall.AbstractRobotRemote.startDefaultMsg)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

		suspend fun doMove(moveShort: String, dest: String) { //Talk with BasicRobotActor
			try {
				val cmd = MoveJsonCmd.get(moveShort)
				val msg: String = ApplMessage.Companion.create("msg(robotmove,dispatch,SENDER,DEST,CMD,1)").toString()
					.replace("SENDER", "actortry")
					.replace("DEST", dest)
					.replace("CMD", cmd!! )
				println("    ---   AbstractRobotRemote | doMove msg:$msg")
				conn.sendALine(msg)
				//moves.updateMovesRep(moveShort);    //WARNING: result unknown
				delay(1000) //to avoid too-rapid movement
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	fun doEnd()  = { state = "end"  }
	fun doSensor(msg : String){ println("robotActorTry should handle: $msg") }
	
	suspend fun doCollision(msg : String){
		println("robotActorTry handles $msg going back a little");
		val goback =  "{ 'type': 'moveBackward', 'arg': 100 }"
		virtualRobotSupport.domove( goback  )  // not for plasticBox : the business logic is more complex ...
		delay(500)		
	}
/*
	fun doMove(msgSplitted : List<String> ){
		val cmd = msgSplitted[1].replace(")","")
		virtualRobotSupport.domove( cmd )		
	}
*/
	while( state == "working" ){
		var msg = channel.receive()
		println("robotActorTry receives: $msg ") //
		//val msgSplitted = msg.split('(')
		//val msgFunctor  = msgSplitted[0]
		val cmd = JSONObject( msg )
		val move = cmd.getString("move")
		//println("robotActorTry msgFunctor $msgFunctor ")
		when( move ){
			"init"      -> doInit()
			"end"       -> doEnd()
			"sensor"    -> doSensor(msg)
			"collision" -> doCollision(msg)
			"move"      -> doMove(move, "robotStepper")
			else        -> println("NO HANDLE for $msg")
		}		
 	}
 	println("robotActorTry ENDS state=$state")
}

suspend fun sendApplCommands(   ) {
	var jsonString = "{ 'move': 'init'  }"
	robotActorTry.send(jsonString)

	val time = 2000L	//time = 1000 => collision
//    for (i in 1..2) {
	jsonString = "{ 'move': 'l'  }"
	robotActorTry.send(jsonString)
	delay(time)

	jsonString = "{ 'move': 'r' }"
	robotActorTry.send(jsonString)
	delay(1000)

	jsonString = "{ 'move': 'end'  }"
	robotActorTry.send(jsonString)
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
	sendApplCommands(   )
	println("BYE")
}
 

