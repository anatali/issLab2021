package robotVirtual

import org.json.JSONObject
import it.unibo.kactor.*
import it.unibo.kactor.MsgUtil
import unibo.comm22.ws.WsConnSysObserver
import unibo.comm22.utils.CommUtils


/*
  Oggetto che informa l'owner in caso di collisione
*/ 
class WsSupportObserver( val owner:String) : WsConnSysObserver( owner) {
 var stepok = MsgUtil.buildDispatch("wsobs","stepok","stepok(done)",owner )
 var stepko = MsgUtil.buildDispatch("wsobs","stepko","stepko(todo)",owner )

	
	override fun update( data : String ) {
 		//ColorsOut.outappl("WsConnSysObserver update receives:$data $actionDuration", ColorsOut.GREEN);
        val msgJson = JSONObject(data)
        //println("       &&& WsSupportObserver  | update msgJson=$msgJson" ) //${ aboutThreads()}
		val ownerActor = sysUtil.getActor(owner)
		if( ownerActor == null ) {
			val ev = CommUtils.buildEvent( "wsconn", "updateev", data  ); //TODO JUNE22
            println("       &&& WsSupportObserver  | ownerActor null ev=$ev" ) 
		}
		if( msgJson.has("target")){
			//TODO JUNE22
				/*
				runBlocking {
					var target = msgJson.getString("target")
					ownerActor!!.emit("obstacle","obstacle($target)")
				}*/
		}
	}
	

}