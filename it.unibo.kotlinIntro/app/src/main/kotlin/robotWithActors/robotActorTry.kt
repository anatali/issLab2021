/*
robotActorTry.kt

 */

package robotWithActors
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.ApplMessage
import it.unibo.robotService.ApplMsgs
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import mapRoomKotlin.TripInfo
import org.json.JSONObject
import prodCons.curThread
import java.lang.Exception
import java.util.HashMap

val MoveJsonCmd : HashMap<String, String> = hashMapOf(
	"w" to ApplMsgs.forwardMsg.replace(",", "@"),
	"s" to ApplMsgs.backwardMsg.replace(",", "@"),
	"l" to ApplMsgs.turnLeftMsg.replace(",", "@"),
	"r" to ApplMsgs.turnRightMsg.replace(",", "@"),
	"h" to ApplMsgs.haltMsg.replace(",", "@")
)

lateinit var robotActorTry: SendChannel<String>

fun connectToRobotViaTcp() : IConnInteraction {
	println("robotActorTry | connectToRobotViaTcp ${curThread()}" )
	//try {
		val fp = FactoryProtocol(null, "TCP", "robot")
//println("    ---  robotActorTry | fp:$fp")
		val conn = fp.createClientProtocolSupport("localhost", 8010)
		println("    --- connectToRobotViaTcp | connected:$conn")
		InputReader.createInputReader( robotActorTry, conn )
		return conn
	//} catch (e: Exception) { println("robotActorTry doInit error $e ") }
}//connectToRobotViaTcp

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun createActor(scope:CoroutineScope) : SendChannel<String> {
	 robotActorTry = scope.actor {
			val moves = TripInfo()
			var state = "working"
         	lateinit var conn: IConnInteraction

			fun doInit(){
				conn = connectToRobotViaTcp()
 			}

			suspend fun doMove(moveShort: String, dest: String) { //Talk with BasicRobotActor
				try {
					//println("    ---   robotActorTry | doMove moveShort:$moveShort  " )
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

 			fun doSensor(msg: String) {
				println("robotActorTry should handle: $msg")
			}

			suspend fun doCollision(msg: String) {
				println("robotActorTry | doCollision $msg  ");
 			}//doCollision
/*
msg-driven behavior
 */
			while (state == "working") {
				var msg = channel.receive()
				println("robotActorTry working receives: $msg ") //
				try {
					val cmd = JSONObject(msg)
					//println("robotActorTry cmd: $cmd ") //
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
						"end"       -> { state = "end" }
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




 

