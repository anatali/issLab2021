package robotVirtual
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class NaiveActorKotlinObserver(name: String, scope:CoroutineScope)
    : ActorBasicKotlin(name, scope) {

    override protected suspend fun handleInput(msg : ApplMessage){
        var msgJsonStr = msg.msgContent
        val msgJson = JSONObject(msgJsonStr)
        println("$name  | msgJson=$msgJson  ${ aboutThreads()} " )
    }
}