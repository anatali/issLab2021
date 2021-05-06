package kotlinCode

import it.unibo.actor0.ApplMessage
import org.json.JSONObject

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
	
}