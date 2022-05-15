package robotVirtual
 
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import it.unibo.supports.*
import it.unibo.kactor.*
import it.unibo.actor0.*
import it.unibo.actor0.ApplMessage
import it.unibo.kactor.MsgUtil 
 
/*
 Il robot che fa w prosegue fino a che non riceve h
*/ 
class WsSupportObserver(name: String, scope:CoroutineScope, val owner:ActorBasic)
    : ActorBasicKotlin(name, scope) {
 var stepok = MsgUtil.buildDispatch("wsobs","stepok","stepok(done)",owner.getName())
 var stepko = MsgUtil.buildDispatch("wsobs","stepko","stepko(todo)",owner.getName())
	
    override protected suspend fun handleInput(msg : ApplMessage){
        var msgJsonStr = msg.msgContent
        val msgJson = JSONObject(msgJsonStr)
        println("       &&& $name  | handleInput msgJson=$msgJson" ) //${ aboutThreads()}
		/*
		if( msgJson.has("endmove") && msgJson.getBoolean("endmove") ) {
			if( msgJson.getString("move").equals("moveForward") ){
				//owner.autoMsg()
				println("WsSupportObserver send $stepok")
				owner.autoMsg(stepok)
			}else{
				//println("WsSupportObserver TODO ${msgJson.getBoolean("endmove") }" )
				
			}
		} */
		if( msgJson.has("collision")) owner.emit("obstacle","obstacle(virtual)")
    }
}