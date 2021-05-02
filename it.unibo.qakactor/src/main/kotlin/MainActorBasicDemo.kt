/*
 MainActorBasicDemo
 */
package it.unibo.kactor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import java.io.File


class ApplActor( name:String, scope: CoroutineScope) : ActorBasic(name, scope){
override suspend fun actorBody(msg : ApplMessage){
 			File("${name}_MsgLog.txt").appendText("${msg}\n")
	}	
}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking {
	println("MainActorBasicDemo START")
	sysUtil.logMsgs = true
	val qaappl = ApplActor("qaappl", this)
	MsgUtil.sendMsg("cmd", "cmd(w)", qaappl)
	qaappl.emit("alarm", "alarm(fire)")
	println("MainActorBasicDemo END")
}