package mapRoomKotlin


fun doTestMove(){
	mapUtil.showMap()
	//Going down
	for( i in 1..3) mapUtil.doMove( "w"  )
//	println( "-----------------------------------"  );
	mapUtil.showMap()
 	mapUtil.doMove("l")
	for( i in 1..3) mapUtil.doMove( "w"  )
	for( i in 1..3) mapUtil.doMove( "s"  )
//	println( "-----------------------------------"  );
	mapUtil.showMap()
	/*
	//Obstacle
	mapUtil.setObstacle(  )
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

fun main(){
	doTestMove()
}
 		