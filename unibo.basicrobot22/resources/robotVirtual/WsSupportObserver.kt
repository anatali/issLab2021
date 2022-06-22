package robotVirtual

import org.json.JSONObject
import it.unibo.kactor.*
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking
import unibo.comm22.utils.ColorsOut
import unibo.comm22.ws.WsConnSysObserver
import unibo.comm22.utils.CommUtils


/*
  Oggetto che informa l'owner in caso di collisione
*/ 
class WsSupportObserver( val owner:String) : WsConnSysObserver( owner) {
 var stepok = MsgUtil.buildDispatch("wsobs","stepok","stepok(done)",owner )
 var stepko = MsgUtil.buildDispatch("wsobs","stepko","stepko(todo)",owner )

	
	override fun update( data : String ) {
 		ColorsOut.outappl("WsSupportObserver update $data owner=$owner   ", ColorsOut.MAGENTA);
        val msgJson = JSONObject(data)
        //println("       &&& WsSupportObserver  | update msgJson=$msgJson" ) //${ aboutThreads()}
		val ownerActor = sysUtil.getActor(owner)
		if( ownerActor == null ) {
			val ev = CommUtils.buildEvent( "wsconn", "wsEvent", data  );
            println("       &&& WsSupportObserver  | ownerActor null ev=$ev" )
			return
		}
		var target : String = "nonso"
		if( msgJson.has("target")   ){
			target = msgJson.getString("target")
			runBlocking {
				ownerActor!!.emit("obstacle","obstacle($target)")
			}
		}
		if( msgJson.has("collision") ){
			target = msgJson.getString("move")
			runBlocking {
				ownerActor!!.emit("obstacle","obstacle($target)")
			}
		}


	}
	

}