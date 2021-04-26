package mapRoomKotlin


fun doTestMove(){
	mapUtil.showMap()
	//Going down
	for( i in 1..3) mapUtil.doMove( "w"  )
//	println( "-----------------------------------"  );
	mapUtil.showMap()
 	mapUtil.doMove("l")
	for( i in 1..3) mapUtil.doMove( "w"  )
//	println( "-----------------------------------"  );
	mapUtil.showMap()
	//Obstacle
	mapUtil.setObstacle(  )
	mapUtil.showMap()

	for( i in 1..3) mapUtil.doMove( "s"  )

	/*
  	mapUtil.doMove("s")
	println( "-----------------------------------"  );
	mapUtil.showMap()
	//Going up
 	mapUtil.doMove("l")
	for( i in 1..3) mapUtil.doMove( "w"  )
	println( "-----------------------------------"  );
	mapUtil.showMap()

	 */
}

fun doTestTripInfo(){
	val moves = TripInfo()
	moves.showMap()
	moves.updateMovesRep("w")
	moves.updateMovesRep("s")
	moves.showMap()
}
fun main(){
	doTestMove()
	//doTestTripInfo()
}
 		