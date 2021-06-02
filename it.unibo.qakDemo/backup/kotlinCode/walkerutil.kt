package kotlinCode
import itunibo.planner.*
import mapRoomKotlin.mapUtil
import it.unibo.kactor.ActorBasicFsm
import org.json.JSONObject
object walkerutil{
	 
	
	fun doPlan( xs: String, ys:String ) : String{
		plannerUtil.planForGoal(xs,ys)
		val Moves = plannerUtil.getActions()
		var path = ""
		Moves.forEach({path=path+it})
		//println("walkerutil | path=$path")
		val PathTodo    = "{\"path\":\"$path\"}"
		return PathTodo
	}
	
	fun updateMapOk(mapname: String="walkermap", show: Boolean = true ){
		val path = pathexecutil.pathDone.replace("p","w")
		path.forEach {  plannerUtil.updateMap(""+it) }
		plannerUtil.showMap()
		plannerUtil.saveRoomMap(mapname)
	}
	
	fun updateMapKO(mapname: String="walkermap", show: Boolean = true ){
		val path = pathexecutil.pathDone.replace("p","w")
		path.forEach {  plannerUtil.updateMap(""+it) }
		plannerUtil.updateMapObstacleOnCurrentDirection()
		plannerUtil.showMap()
		plannerUtil.saveRoomMap(mapname)
	}
	
	suspend fun execMove(moveTodo: String, master: ActorBasicFsm){
		//println("walkerutil | execMove moveTodo=$moveTodo")
		val MoveAnsw = kotlinCode.CallRestWithApacheHTTP.doMove(moveTodo)
		println("walkerutil | MoveAnsw=$MoveAnsw")
 		val answJson = JSONObject( MoveAnsw )
		println("walkerutil | answJson=$answJson")
		if( ( answJson.has("endmove") && answJson.getString("endmove") == "true")
			|| answJson.has("stepDone") ){			 
			master.autoMsg("moveok","move($moveTodo)")
		}else{
			master.autoMsg("movefail","moveko($moveTodo)")

		}
	}	
	 
}

