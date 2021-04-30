package it.unibo.webspring.demo
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class DoNothingObserver(name: String, scope:CoroutineScope)
    : ActorBasicKotlin(name, scope) {

    override suspend fun handleInput(msg : ApplMessage){
    }
}