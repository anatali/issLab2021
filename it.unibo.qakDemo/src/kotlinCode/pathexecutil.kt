package kotlinCode

import it.unibo.actor0.ApplMessage
import org.json.JSONObject
import it.unibo.kactor.ActorBasicFsm
import java.util.Scanner
import alice.tuprolog.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import it.unibo.supports.IssWsHttpKotlinSupport
 

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
		//println("pathexecutil | memoCurPath msg=$msg")
		//PROLOG version
		/*
		val ap = it.unibo.kactor.ApplMessage(msg)
		val cc = ap.msgContent()	//a String
		println("pathexecutil | PROLOG ap=$ap cc=$cc ")
		val cct = Term.createTerm(cc)
		println("pathexecutil | PROLOG ap=$ap cc=$cc cct=$cct")
		*/
		//2021 version
		val am      = ApplMessage.create(msg)
		val content = am.msgContent
		//println("pathexecutil | memoCurPath content=$content")	//{"path":"wwlw", "owner":"textexec"}
		val amJson = JSONObject( content )
		curPath    = amJson.get("path").toString().replace("w","p")
		//owner      = amJson.get("owner").toString()
		pathDone   = ""
		println("pathexecutil | memoCurPath curPath=$curPath ")
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
		delay(150)  	//to reduce path speed
		if( move.length == 0 ) {
			master.autoMsg("pathdone","pathtodo($curPath)")
			//println("!!!!!!!!!!! SEND pathdone to OWNER=$owner" )
		}else{
			doMove(master, move)
		}
	} 

	suspend fun doMove(master: ActorBasicFsm, moveTodo: String ){
		//println("pathexecutil | doMove moveTodo=$moveTodo")
		val MoveAnsw = kotlinCode.CallRestWithApacheHTTP.doMove(moveTodo)
		//println("pathexecutil | doMove $moveTodo MoveAnsw=$MoveAnsw")
 
		val answJson = JSONObject( MoveAnsw )
		println("pathexecutil | doMove $moveTodo answJson=$answJson")
		if( ( answJson.has("endmove") && answJson.getString("endmove") == "true")
			|| answJson.has("stepDone") ){
			pathDone = pathDone+moveTodo
			master.autoMsg("moveok","move($moveTodo)")
		}else{
			master.autoMsg("pathfail","pathdone($pathDone)")
			//println("!!!!!!!!!!!  SEND pathfail to OWNER=$owner")
		}
	}
	
	
	fun createSonarObserver(scope: CoroutineScope ){
		val support = IssWsHttpKotlinSupport.createForWs(scope, "localhost:8091" )
		val obs     = SonarObserver("sonarobs", scope )
		support.registerActor( obs )
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