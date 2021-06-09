package robotMock

object mockrobotSupport{
	
	fun simulateMove(move : String ){
		println("		%%% mockrobotSupport | doing $move")
	}
	
	fun create(){
		println("		%%% mockrobotSupport | CREATED")
	}
	
		fun  move( cmd : String ){
		//println("  	%%% mbotSupport | move cmd=$cmd ")
		when( cmd ){
			"msg(w)", "w" -> simulateMove("w")
			"msg(s)", "s" -> simulateMove("s")
			"msg(a)", "a" -> simulateMove("a")
			"msg(d)", "d" -> simulateMove("d")
			"msg(l)", "l" -> simulateMove("l") //sendToPython("l")	//
			"msg(r)", "r" -> simulateMove("r") //sendToPython("r")	//
			"msg(z)", "z" -> simulateMove("z") //sendToPython("z")
			"msg(x)", "x" -> simulateMove("x") //sendToPython("x")	//
			"msg(h)", "h" -> simulateMove("h")
			else -> println("		%%% mockrobotSupport | command $cmd unknown")
		}
		
	}

}