package mapRoomKotlin


fun doTestMove(){
	mapUtil.showMap()
	//Going down
	for( i in 1..3) mapUtil.doMove( "w"  )
//	println( "-----------------------------------"  );
//	println( "$map"  );
 	mapUtil.doMove("l")
	for( i in 1..3) mapUtil.doMove( "w"  )
//	println( "-----------------------------------"  );
//	println( "$map"  );
	//Obstacle
	//mapUtil.setObstacle(  )
	mapUtil.doMove("o")

  	mapUtil.doMove("s")
	println( "-----------------------------------"  );
	mapUtil.showMap()
	//Going up
 	mapUtil.doMove("l")
	for( i in 1..3) mapUtil.doMove( "w"  )
	println( "-----------------------------------"  );
	mapUtil.showMap()
}

fun main(){
	doTestMove()
}
 		