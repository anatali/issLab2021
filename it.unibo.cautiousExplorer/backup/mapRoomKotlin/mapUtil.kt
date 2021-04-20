package mapRoomKotlin

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor

object mapUtil{
 	var state = RobotState(0,0,Direction.DOWN)
	var map   = RoomMap.getRoomMap()

    fun colorPrint(msg : String, color : Color = Color.LIGHT_GRAY ){
        println(Kolor.foreground("      $msg", color ) )
    }
    fun colorPrintNoTab(msg : String, color : Color = Color.BLUE ){
        println(Kolor.foreground("$msg", color ) )
    }

    @JvmStatic fun getMapAndClean() : String{ //(fName : String="storedMap.txt")
		val outS = map.toString()
		RoomMap.resetRoomMap()
		return outS
	}


//=================================================================================
    @JvmStatic fun getMapRep() : String{
        return map.toString()
    }
    @JvmStatic fun getDirection() : String{
        return state.direction.toString()
    }

    private fun setObstacleOnCell(){
		map.put( state.x,  state.y, Box(true, false, false))
	}
    @JvmStatic fun setObstacle() {  //trick!!
        doMove("w")
        setObstacleOnCell()
        doMove("s")
    }
    @JvmStatic fun doMove(move: String ) {
       val x = state.x
       val y = state.y
//       colorPrint("doMove move=$move  dir=${state.direction} x=$x y=$y dimMapX=$map.dimX{} dimMapY=${map.dimY}")
       try {
            when (move) {
                "w" -> {
                     map.put(x, y, Box(false, false, false)) //clean the cell
					 state = state.forward();
                     map.put(state.x, state.y, Box(false, false, true))
                }
                "s" -> {
                     map.put(x, y, Box(false, false, false)) //clean the cell
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
		   
//		   colorPrint( "$map"  )
        } catch (e: Exception) {
           colorPrint("doMove: ERROR:" + e.message)
        }
	}

    @JvmStatic fun showMap(){
        colorPrintNoTab( "$map ${state}", Color.LIGHT_RED  )
	}
	
}