package kotlinCode

import it.unibo.kactor.ActorBasic
import org.json.JSONObject

object pathExec{

	suspend fun doPlannedMoves(path: String, master: ActorBasic){
		val answer = CallRestWithApacheHTTP.doPath(path)
		println("doPlannedMoves answer=$answer")
		val answJson = JSONObject( answer )
		if( answJson.has("executorDone")){
			val path = answJson.getString("executorDone")
			println("doPlannedMoves automsg to $master")
			//master.emit("pathDone","pathDone($path)")
			master.autoMsg("pathDone","pathDone($path)")
		}

	}
	suspend fun doJob(n: Int, master: ActorBasic){
		val ns = ""+n
		itunibo.planner.plannerUtil.planForGoal(ns,ns)
		val Moves = itunibo.planner.plannerUtil.getActions()
		//println("pathExec MOVES=$Moves")
		var path = ""
		Moves.forEach({path=path+it})
		//println("pathExec PATH=$path")
		doPlannedMoves(path, master)
		/*
		val answer = CallRestWithApacheHTTP.doPath(path)
		println("pathExec answer=$answer")
		
		val answJson = JSONObject( answer )
		
		if( answJson.has("executorDone")){
			val path = answJson.getString("executorDone")
			println("pathExec automsg to $master")
			//master.emit("pathDone","pathDone($path)")
			master.autoMsg("pathDone","pathDone($path)")
		} */
	}
 

}