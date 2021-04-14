package virtualRobotUsage
//robotActorTry.kt

//import kotlinx.coroutines.runBlocking
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.ApplMessage
import it.unibo.robotService.ApplMsgs
import it.unibo.supports.FactoryProtocol
import kotlindemo.cpus
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import mapRoomKotlin.TripInfo
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

//Actor that includes the business logic; the behavior is message-driven 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun createActor(scope:CoroutineScope) : SendChannel<String> {
	val robotActorTry: SendChannel<String> =
		scope.actor {
			val moves = TripInfo()
			var state = "working"
			val MoveJsonCmd = HashMap<String, String>()
			lateinit var conn: IConnInteraction

			fun doInit() {
				try {
					println("robotActorTry doInit1  ${ApplMsgs.forwardMsg} MoveJsonCmd=$MoveJsonCmd") //
			MoveJsonCmd.put("w", ApplMsgs.forwardMsg.replace(",", "@"))
			MoveJsonCmd.put("s", ApplMsgs.backwardMsg.replace(",", "@"))
			MoveJsonCmd.put("l", ApplMsgs.turnLeftMsg.replace(",", "@"))
			MoveJsonCmd.put("r", ApplMsgs.turnRightMsg.replace(",", "@"))
			MoveJsonCmd.put("h", ApplMsgs.haltMsg.replace(",", "@"))
					println("robotActorTry doInit2 $MoveJsonCmd ") //

					val fp = FactoryProtocol(null, "TCP", "robot")
					println("    ---  robotActorTry | fp:$fp")
					conn = fp.createClientProtocolSupport("localhost", 8010)
					println("    --- robotActorTry | connected:$conn")
					//reader = ConnectionReader("reader", conn)
					//reader.registerActor(this)
					//reader.send(it.unibo.remoteCall.robotActorTry.startDefaultMsg)
				} catch (e: Exception) {
					println("robotActorTry doInit error $e ")
					e.printStackTrace()
				}
			}//doInit

			suspend fun doMove(moveShort: String, dest: String) { //Talk with BasicRobotActor
				try {
					val cmd = MoveJsonCmd.get(moveShort)
					val msg: String =
						ApplMessage.Companion.create("msg(robotmove,dispatch,SENDER,DEST,CMD,1)").toString()
							.replace("SENDER", "actortry")
							.replace("DEST", dest)
							.replace("CMD", cmd!!)
					println("    ---   robotActorTry | doMove msg:$msg")
					conn.sendALine(msg)
					moves.updateMovesRep(moveShort);
					moves.showMap()
					moves.showJourney()
					delay(1000) //to avoid too-rapid movement
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}//doMove

			fun doEnd() = { state = "end" }
			fun doSensor(msg: String) {
				println("robotActorTry should handle: $msg")
			}

			suspend fun doCollision(msg: String) {
				println("robotActorTry handles $msg going back a little");
				val goback = "{ 'type': 'moveBackward', 'arg': 100 }"
				virtualRobotSupport.domove(goback)  // not for plasticBox : the business logic is more complex ...
				delay(500)
			}//doCollision

			while (state == "working") {
				var msg = channel.receive()
				println("robotActorTry receives: $msg ") //
				//val msgSplitted = msg.split('(')
				//val msgFunctor  = msgSplitted[0]
				try {
					val cmd = JSONObject(msg)
					println("robotActorTry cmd: $cmd ") //
					val move = cmd.getString("move")
					println("robotActorTry move=$move ")
					when (move) {
						"init" -> doInit()
						"end" -> doEnd()
						"sensor" -> doSensor(msg)
						"collision" -> doCollision(msg)
						"l","r","w","s","h" -> doMove(move, "stepRobot")
						else -> println("NO HANDLE for $msg")
					}
				} catch (e: Exception) {
					println("robotActorTry error $e ")
				}
			}//while

			println("robotActorTry ENDS state=$state")
		}//actor
	return robotActorTry
}
suspend fun sendApplCommands( scope:CoroutineScope  ) {

	val robotActorTry = createActor(scope)

	var jsonString = "{ \"move\": \"init\" }"
	robotActorTry.send(jsonString)
	delay(1000)
	println("    ---  robotActorTry | turnLeft")
	robotActorTry.send("{ \"move\": \"l\"  }")
	robotActorTry.send("{ \"move\": \"r\"  }")
	robotActorTry.send("{ \"move\": \"w\"  }")
	robotActorTry.send("{ \"move\": \"l\"  }")
	robotActorTry.send("{ \"move\": \"w\"  }")
	robotActorTry.send("{ \"move\": \"w\"  }")
	robotActorTry.send("{ \"move\": \"s\"  }")	//???


/*
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
	
 */
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
	println("BEGINS CPU=$cpus ${kotlindemo.curThread()}")
	runBlocking {
		sendApplCommands(this)
		//delay(15000)
		println("ENDS runBlocking ${kotlindemo.curThread()}")
	}
	println("ENDS main ${kotlindemo.curThread()}")
}
 

