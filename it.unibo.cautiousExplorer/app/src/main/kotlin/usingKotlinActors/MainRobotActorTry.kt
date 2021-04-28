//MainRobotActorTry.kt

package usingKotlinActors
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel


fun readCmd() : String { print("move>"); return readLine()!! }

suspend fun userCmd( input : String, myrobot: SendChannel<String> ) {
	println("userCmd $input")
	when( input ){
		"w","s","l","r","h" ->  myrobot.send("{ \"move\": \"$input\"  }")
		else ->  { println("command unknown") }
	}
 }


suspend fun sendApplCommands( scope:CoroutineScope  ) {
	val myrobot = createActor(scope)
	var jsonString = "{ \"cmd\": \"init\" }"
	myrobot.send(jsonString)
	myrobot.send("{ \"move\": \"l\"  }")
	myrobot.send("{ \"move\": \"r\"  }")

	scope.launch(Dispatchers.IO){
		println("sendApplCommands wait for input ... ${sysUtil.curThread()}")
		var input =  readCmd()
		while( input != "z" ){
			userCmd( input,  myrobot)
			input    =  readCmd()
		}
		myrobot.send("{ \"cmd\": \"end\"  }")
	}

}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
	runBlocking {
		println("main BEGINS ${sysUtil.curThread()}")
		sendApplCommands(this)
		println("main ENDS runBlocking ${sysUtil.curThread()}")
	}
	println("main ENDS ${sysUtil.curThread()}")
}
 

