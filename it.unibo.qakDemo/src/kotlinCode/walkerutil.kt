package kotlinCode
import itunibo.planner.*
import mapRoomKotlin.mapUtil
object walkerutil{
	 
	
	fun doPlan( ns: String ) : String{
		plannerUtil.planForGoal(ns,ns)
		val Moves = plannerUtil.getActions()
		var path = ""
		Moves.forEach({path=path+it})
		//println("walkerutil | path=$path")
		val PathTodo    = "{\"path\":\"$path\"}"
		return PathTodo
	}
	
	fun updateMapOk(mapname: String="walkermap", show: Boolean = true ){
		val path = pathexecutil.pathDone
		path.forEach {  plannerUtil.updateMap(""+it) }
		plannerUtil.showMap()
		plannerUtil.saveRoomMap(mapname)
	}
	
	fun updateMapKO(mapname: String="walkermap", show: Boolean = true ){
		val path = pathexecutil.pathDone
		path.forEach {  plannerUtil.updateMap(""+it) }
		plannerUtil.updateMapObstacleOnCurrentDirection()
		plannerUtil.showMap()
		plannerUtil.saveRoomMap(mapname)
	}
	
}

