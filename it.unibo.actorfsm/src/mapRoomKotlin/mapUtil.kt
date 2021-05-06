package mapRoomKotlin

object mapUtil{
 	private var state    = RobotState(0,0,Direction.DOWN)	 
	var map              = RoomMap.getRoomMap()
	var refMapForTesting = buildRefTestMap()
	
	
	
 	fun buildRefTestMap() : String{
		val nr = 4
		val nc = 5
 			for( i in 1..nr ) mapUtil.doMove("w")
			mapUtil.doMove("l")
		    for( i in 1..nc ) mapUtil.doMove("w")
			mapUtil.doMove("l")
			for( i in 1..nr ) mapUtil.doMove("w")
			mapUtil.doMove("l")
		    for( i in 1..nc ) mapUtil.doMove("w")
			mapUtil.doMove("l")
			val res = mapUtil.map.toString()
			RoomMap.resetRoomMap()
 			//println( "buildRefTestMap DONE $res" )
			return res
  	}
	
	
	fun setObstacle(){
		map.put( state.x,  state.y, Box(true, false, false))
	}
	
    fun doMove(move: String ) {
       val x = state.x
       val y = state.y
//       println("doMove move=$move  dir=${state.direction} x=$x y=$y dimMapX=$map.dimX{} dimMapY=${map.dimY}")
       try {
            when (move) {
                "w" -> {
                     map.put(x, y, Box(false, false, false)) //clean the cell
					 state = state.forward();
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "s" -> {
	                 state = state.backward();
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "a"  -> {
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "l" -> {
					  state = state.turnLeft();
                      map.put(state.x, state.y, Box(false, false, true))
                }
                "d" -> {
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "r" -> {
 					state = state.turnRight();
                    map.put(state.x, state.y, Box(false, false, true))
                }
 
		   }//switch
		   
//		   println( "$map"  )
        } catch (e: Exception) {
            println("mapUtil | doMove: ERROR:" + e.message)
		    println("doMove move=$move  dir=${state.direction} x=$x y=$y dimMapX=$map.dimX{} dimMapY=${map.dimY}")
        }
	}
	
	fun showMap(){
		println( "$map"  )
	}
	
}