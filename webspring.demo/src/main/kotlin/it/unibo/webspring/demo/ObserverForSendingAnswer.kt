package it.unibo.webspring.demo
import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope

class ObserverForSendingAnswer(
    name: String, scope:CoroutineScope,
    val callback:( String )-> Unit
    //val controller: M2MRestController? = null,
    //var owner: ActorBasicKotlin? = null
    ) : ActorBasicKotlin(name, scope) {

    override suspend fun handleInput(msg : ApplMessage){
        colorPrint("$name msg=$msg"  , Color.MAGENTA)
        //controller?.answer = msg.msgContent
        callback( msg.msgContent )
        //owner?.send(msg)
        //result = msg.msgContent
    }


}