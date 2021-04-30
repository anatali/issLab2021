package it.unibo.webspring.demo
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class ObserverForSendingAnswer(name: String, scope:CoroutineScope)
    : ActorBasicKotlin(name, scope) {

    var result = ""

    override suspend fun handleInput(msg : ApplMessage){
        println("$name msg=$msg")
        result = msg.msgContent
    }


}