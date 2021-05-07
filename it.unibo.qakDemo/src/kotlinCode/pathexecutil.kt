package kotlinCode

import it.unibo.actor0.ApplMessage
import org.json.JSONObject
import it.unibo.kactor.ActorBasicFsm
import java.util.Scanner
import alice.tuprolog.*
import com.andreapivetta.kolor.*

object pathexecutil{
var pathDone = ""
var curPath  = ""
lateinit var  master: ActorBasicFsm
lateinit var  owner:  String

	fun register( actor: ActorBasicFsm ){
		master = actor
	}

	fun getCurrentPath(  ) : String {
		return curPath
	}

	fun memoCurPath( msg: String  ){
		//msg(dopath,dispatch,testexec,pathexec,'dopath(path({"path":"wwlw"}),caller)',8)
		println("pathexecutil | msg=$msg")
		//PROLOG version
		val ap = it.unibo.kactor.ApplMessage(msg)
		val cc = ap.msgContent()	//a String
		//println("pathexecutil | PROLOG ap=$ap cc=$cc ")
		//val cct = Term.createTerm(cc)
		//println("pathexecutil | PROLOG ap=$ap cc=$cc cct=$cct")
		//2021 version
		val am      = ApplMessage.create(msg)
		val content = am.msgContent
		println("pathexecutil | content=$content")	//{"path":"wwlw", "owner":"textexec"}
		val amJson = JSONObject( content )
		curPath    = amJson.get("path").toString()
		owner      = amJson.get("owner").toString()
		pathDone  = ""
		println("pathexecutil | curPath=$curPath owner=$owner")
	}

	fun nextMove() : String{
		//println("pathexecutil | nextMove curPath=$curPath")
		if( curPath.length == 0 ) return ""
		//curPath still has moves
		val move = ""+curPath[0]
		curPath  = curPath.substring(1)
		return move
	}

	suspend fun doNextMove( master: ActorBasicFsm) {
		val move = nextMove()
		//println("pathexecutil | doNextMove=$move")
		if( move.length == 0 ) {
			master.autoMsg("pathdone","pathtodo($curPath)")
			println("!!!!!!!!!!! SEND pathdone to OWNER=$owner" )
		}else{
			doMove(move, master)
		}
	}

	suspend fun doMove(moveTodo: String, master: ActorBasicFsm){
		val MoveAnsw = kotlinCode.CallRestWithApacheHTTP.doMove(moveTodo)
		val answJson = JSONObject( MoveAnsw )
		println("pathexecutil | answJson=$answJson")
		if( answJson.has("endmove") && answJson.getString("endmove") == "true"){
			pathDone = pathDone+moveTodo
			master.autoMsg("moveok","move($moveTodo)")
		}else{
			//master.autoMsg("movefail","move($moveTodo)")
			master.autoMsg("pathfail","pathdone($pathDone)")
			println("!!!!!!!!!!!  SEND pathfail to OWNER=$owner")
		}
	}

	fun waitUser(prompt: String) {
		print(">>>  $prompt >>>  ")
		val scanner = Scanner(System.`in`)
		try {
			scanner.nextInt()
		} catch (e: java.lang.Exception) {
			e.printStackTrace()
		}
	}
}