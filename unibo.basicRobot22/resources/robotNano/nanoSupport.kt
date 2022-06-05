package robotNano

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import robotMbot.mbotSupport
import java.io.BufferedOutputStream
import java.io.OutputStreamWriter

object nanoSupport {
	lateinit var writer : OutputStreamWriter
	val rotLeftTime : Long   = 610;
	val rotRightTime : Long  = 610;
	val rotZStepTime  = 58;

	fun create( owner : ActorBasic ){		
		val p = Runtime.getRuntime().exec("sudo ./Motors")		 	
 		writer = OutputStreamWriter(  p.getOutputStream()  )
		println("motorscSupport | CREATED with writer=$writer")
 	}

	fun move( cmd : String="" ){
		//println("motorscSupport | WRITE $msg with $writer")
		writer.write( cmd )  //delay della rotazione incorporato in Motors.c
		writer.flush()
		/*
		when( cmd ){
			"msg(w)", "w" -> writer.write( cmd )
			"msg(s)", "s" -> writer.write( cmd )
			"msg(a)", "a" -> { kotlinx.coroutines.runBlocking{ writer.write("a") ;  kotlinx.coroutines.delay(
				mbotSupport.rotLeftTime
			);   writer.write("h") } }
			"msg(d)", "d" -> { kotlinx.coroutines.runBlocking{ writer.write("r") ;  kotlinx.coroutines.delay(
				mbotSupport.rotLeftTime
			);   writer.write("h") } }
			"msg(l)", "l" -> { kotlinx.coroutines.runBlocking{ writer.write("l") ;  kotlinx.coroutines.delay(
				mbotSupport.rotLeftTime
			);   writer.write("h") } }
			"msg(r)", "r" -> { kotlinx.coroutines.runBlocking{ writer.write("r") ;  kotlinx.coroutines.delay(
				mbotSupport.rotRightTime
			);  writer.write("h") } }
			"msg(z)", "z" -> writer.write("z")
			"msg(x)", "x" -> writer.write("x")
			"msg(h)", "h" -> writer.write("h")
			else -> println("   	%%% mbotSupport | command $cmd unknown")
		}
		writer.flush()*/
	}
	
	fun terminate(){
		
	}

}