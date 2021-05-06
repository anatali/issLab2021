package demoRobotWithPlanner
import com.andreapivetta.kolor.Color
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel

class MarchegianiObserverForSendingAnswer(
    name: String, scope:CoroutineScope,
    val channel: Channel<String>,
    var owner: ActorBasicKotlin? = null     //used by PathExecutor
    ) : ActorBasicKotlin(name, scope) {

    override suspend fun handleInput(msg : ApplMessage){
        colorPrint("$name  msg=$msg", Color.MAGENTA)
        owner?.send(msg)   //interact with the owner (only)
        if( owner == null ) //not working for an owner => working for the m2m
            channel.send( msg.msgContent )
    }


}