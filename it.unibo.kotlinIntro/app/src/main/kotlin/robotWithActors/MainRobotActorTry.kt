//robotActorTry.kt

package robotWithActors

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
//lateinit var myrobot : SendChannel<String>

suspend fun sendApplCommands( scope:CoroutineScope  ) {
	val myrobot = createActor(scope)


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
 

