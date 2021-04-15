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
import prodCons.curThread
import java.lang.Exception
import java.util.HashMap

/*
Actor that includes the business logic; the behavior is message-driven
 */
lateinit var myrobot : SendChannel<String>

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
println("robotActorTry | doInit1 ${curThread()}" )
			try {
				MoveJsonCmd.put("w", ApplMsgs.forwardMsg.replace(",", "@"))
				MoveJsonCmd.put("s", ApplMsgs.backwardMsg.replace(",", "@"))
				MoveJsonCmd.put("l", ApplMsgs.turnLeftMsg.replace(",", "@"))
				MoveJsonCmd.put("r", ApplMsgs.turnRightMsg.replace(",", "@"))
				MoveJsonCmd.put("h", ApplMsgs.haltMsg.replace(",", "@"))
//println("robotActorTry doInit2 $MoveJsonCmd ") //
				val fp = FactoryProtocol(null, "TCP", "robot")
//println("    ---  robotActorTry | fp:$fp")
				conn = fp.createClientProtocolSupport("localhost", 8010)
					println("    --- robotActorTry | connected:$conn")
				createTcpInputReader( conn )
			} catch (e: Exception) {
				println("robotActorTry doInit error $e ")
			}
		}//doInit

			suspend fun doMove(moveShort: String, dest: String) { //Talk with BasicRobotActor
				try {
					println("    ---   robotActorTry | doMove moveShort:$moveShort  " )
					val cmd = MoveJsonCmd.get(moveShort)
					val msg: String =
						ApplMessage.Companion.create("msg(robotmove,dispatch,SENDER,DEST,CMD,1)").toString()
							.replace("SENDER", "actortry")
							.replace("DEST", dest)
							.replace("CMD", cmd!!)
					println("    ---   robotActorTry | doMove msg:$msg ${curThread()}" )
					conn.sendALine(msg)
					moves.updateMovesRep(moveShort);
					moves.showMap()
					moves.showJourney()
					delay(1000) //to avoid too-rapid movement
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}//doMove

			 fun doEndMove( endOfMove: String, move: String ) {
				if( endOfMove=="false") println("$move failed")
			}

			fun doEnd() = { state = "end" }
			fun doSensor(msg: String) {
				println("robotActorTry should handle: $msg")
			}

			suspend fun doCollision(msg: String) {
				println("robotActorTry | doCollision $msg  ");
 			}//doCollision

			while (state == "working") {
				var msg = channel.receive()
				println("robotActorTry working receives: $msg ") //
				try {
					val cmd = JSONObject(msg)
					println("robotActorTry cmd: $cmd ") //
					var input = ""
					var move  = ""
					var endOfMove = "unknown"
					if( cmd.has("collision")){	//first, to avoid premature check on move
						input = "collision"
						move  = cmd.getString("move")
					}else if( cmd.has("endmove")){
						input     = "endmove"
						endOfMove = cmd.getString("endmove")
						move      = cmd.getString("move")
					}else if( cmd.has("move")){
						input = "move"
						move  = cmd.getString("move")
					}else if( cmd.has("cmd")){
						input = cmd.getString("cmd")
					}
					//val move = cmd.getString("move")
					println("robotActorTry input=$input move=$move endOfMove=$endOfMove")
					when (input) {
						"init"      -> doInit()
						"end"       -> doEnd()
						"endmove"   -> doEndMove(endOfMove,move)
						"sensor"    -> doSensor(msg)
						"collision" -> doCollision(msg)
						"move"      -> doMove(move, "stepRobot")
						else -> println("NO HANDLE for $msg")
					}
				} catch (e: Exception) {
					println("robotActorTry error $e ")
				}
			}//while

			println("robotActorTry ENDS state=$state")
		}//actor
	return robotActorTry
}//createActor

/*
Input reader
 */
suspend fun getInput(  conn: IConnInteraction ) {
	while (true) {
		//println("	&&& tcpInputReader  | waitInput ... $conn ${curThread()} "   );
		try {
			val v = conn.receiveALine()    //blocking ...
			println("	&&& tcpInputReader  | $v");
			if (v != null) {
				val msg = ApplMessage.create(v)
				//println("tcpInputReader  | msg $msg ");
				myrobot.send(msg.msgContent.replace("@",","))	//inform myrobot about the result
			} else {
				break
			}
		}catch( e : Exception){
			println("	&&& tcpInputReader  | ERROR: ${e} ")
			break
		}
	} //while
}
fun createTcpInputReader(  conn: IConnInteraction ) {	//: Job
	val inputScope = CoroutineScope( Dispatchers.IO )
	inputScope.launch { getInput(  conn) }
}//createTcpInputReader

suspend fun sendApplCommands( scope:CoroutineScope  ) {
	myrobot = createActor(scope)


	var jsonString = "{ \"cmd\": \"init\" }"
	myrobot.send(jsonString)
	//delay(500)		//embedded in domove
	myrobot.send("{ \"move\": \"l\"  }")
	myrobot.send("{ \"move\": \"r\"  }")

	myrobot.send("{ \"move\": \"w\"  }")
	myrobot.send("{ \"move\": \"l\"  }")
	myrobot.send("{ \"move\": \"w\"  }")
	myrobot.send("{ \"move\": \"w\"  }")
	myrobot.send("{ \"move\": \"s\"  }")

}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
	println("BEGINS CPU=$cpus ${kotlindemo.curThread()}")
	runBlocking {
		sendApplCommands(this)
		println("ENDS runBlocking ${kotlindemo.curThread()}")
	}
	println("ENDS main ${kotlindemo.curThread()}")
}
 

