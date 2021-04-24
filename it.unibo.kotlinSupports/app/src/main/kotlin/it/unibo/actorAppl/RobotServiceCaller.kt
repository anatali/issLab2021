/*
RobotServiceCaller.kt

Implements the interaction via TCP on 8010
Used by AbstractActorCaller
 */

package it.unibo.actorAppl

import com.andreapivetta.kolor.Color
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.sysUtil
import it.unibo.actor0Service.ConnectionReader
import it.unibo.robotService.ApplMsgs
import it.unibo.supports.FactoryProtocol
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap


class RobotServiceCaller(name: String, owner: ActorBasicKotlin) : ActorBasicKotlin(name){
	var conn: IConnInteraction
  
	init{
 		val fp = FactoryProtocol(null, "TCP", name)
		conn   = fp.createClientProtocolSupport("localhost", 8010)
		colorPrint("$name | init connected:$conn", Color.BLUE)
		val reader = ConnectionReader ("reader", conn )
		reader.registerActor(owner)	//the answer received by the reader are redirected to the owner
		reader.send( ApplMsgs.startAny(name))
	}

	suspend fun doMove( msg: String ) {
		try {
			colorPrint("$name | doMove: $msg on $conn ", Color.BLUE )
			conn.sendALine( msg )
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}//doMove


	override suspend fun handleInput(msg: ApplMessage) {
		val input = msg.msgId
		//colorPrint("$name | input: $input ", Color.BLUE)
		when (input) {
			"move"      -> doMove( msg.toString() )
			/*
			The caller does not receive the messages sent by the remote actor
			They are redirected by the ConnectionReader to the owner
			 */
			else -> println("$name | NO HANDLE for $msg")
		}
	}

}



 
