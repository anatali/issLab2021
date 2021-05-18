package kotlinCode
import it.unibo.actor0.ActorBasicKotlin
 
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import it.unibo.kactor.ActorBasic
import it.unibo.actor0.ApplMessage
import it.unibo.kactor.MsgUtil


class SonarObserver(name: String, scope:CoroutineScope   )
    : ActorBasicKotlin(name, scope) {

	
    override protected suspend fun handleInput(msg : ApplMessage){
		println("SonarObserver | msg=$msg")
  		val master = pathexecutil.master
        var msgJsonStr = msg.msgContent 
        val msgJson    = JSONObject(msgJsonStr)
        if( msgJson.has("distance")){
            val distance   = msgJson.getInt("distance").toString()
            println("$name  | msgJson=$msgJson  distance=$distance master=${master.name} ------------- "  )
            val event      = MsgUtil.buildEvent("wenv", "sonar", "distance($distance)" )

            //master.emit(event, true)	//sometimes gives java.lang.ArrayIndexOutOfBoundsException
        }

    }
}