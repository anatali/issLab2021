package kotlinCode

import it.unibo.actor0.ApplMessage
import org.json.JSONObject
import it.unibo.kactor.ActorBasicFsm

object pathexecutil{
var curPath = ""
	
	fun memoCurPath( msg: String  ){
		println("pathexecutil | msg=$msg")
		val am = ApplMessage.create(msg)
		val content = am.msgContent
		println("pathexecutil | content=$content")
		curPath = JSONObject( content ).get("path").toString()
		println("pathexecutil | curPath=$curPath")
	}

	fun nextMove() : String{
		val move = ""+curPath[0]
		curPath  = curPath.substring(1)
		return move
	}
	
	suspend fun doMove(moveTodo: String, master: ActorBasicFsm){
		val MoveAnsw = kotlinCode.CallRestWithApacheHTTP.doMove(moveTodo)
		 
		val answJson = JSONObject( MoveAnsw )
		if( answJson.has("endmove")){	
			master.autoMsg("moveok","move($moveTodo)")
		}else{
			master.autoMsg("movefail","move($moveTodo)")
		}
	}
	
}