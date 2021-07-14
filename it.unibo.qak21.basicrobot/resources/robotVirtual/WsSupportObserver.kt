package robotVirtual
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import it.unibo.kactor.ActorBasic
/*
 A 2021 actor ...
*/ 
class WsSupportObserver(name: String, scope:CoroutineScope, val owner:ActorBasic)
    : ActorBasicKotlin(name, scope) {

    override protected suspend fun handleInput(msg : ApplMessage){
        var msgJsonStr = msg.msgContent
        val msgJson = JSONObject(msgJsonStr)
        //println("       &&&&&&&&&&&&&&&&&&&&& $name  | msgJson=$msgJson" ) //${ aboutThreads()}
		if( msgJson.has("collision")) owner.emit("obstacle","obstacle(virtual)")
    }
}