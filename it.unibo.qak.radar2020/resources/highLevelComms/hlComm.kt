package highLevelComms
/*
  hlComm.kt
 ------------------------------------------------------------------------------------------
 High-level communications library 
 The provided operations 'wrap' lowLevelComms by promoting the idea of
 dispatch (forward), request, answer, event (emit)
 ------------------------------------------------------------------------------------------
*/
import it.unibo.kactor.MsgUtil
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING !!!
 
class hlComm( val conn : IConnInteraction ) {
	
	fun forward( senderName : String, msgId : String, msgContent : String, destName : String  ){
		val m = MsgUtil.buildDispatch(senderName,msgId,msgContent,destName)
		conn.sendALine( m.toString() )
	}
	
	fun request( senderName : String, msgId : String, msgContent : String, destName : String  ){
		val m = MsgUtil.buildRequest(senderName,msgId,msgContent,destName)
		conn.sendALine( m.toString() )
	}

	fun answer( msgId : String, msgContent : String, service : String ="fromcalled", caller : String="tocaller"   ){
		val m = MsgUtil.buildReply(service,msgId,msgContent, caller)
		conn.sendALine( m.toString() )
	}
	
	fun emit( senderName : String, msgId : String, msgContent : String  ){
		val m = MsgUtil.buildEvent(senderName,msgId,msgContent)
		conn.sendALine( m.toString() )
	}
	
	fun receive() : String{ //Blocking
		return conn.receiveALine()
	}
	
}

var hlCommSupport : hlComm?    =  null  //Visible in the package