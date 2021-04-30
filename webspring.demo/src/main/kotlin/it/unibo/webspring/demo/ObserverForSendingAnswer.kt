package it.unibo.webspring.demo
import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class ObserverForSendingAnswer(name: String, scope:CoroutineScope )
    : ActorBasicKotlin(name, scope) {

    var result = "none"

    override suspend fun handleInput(msg : ApplMessage){
        colorPrint("$name msg=$msg"  , Color.MAGENTA)
        result = msg.msgContent
    }


}