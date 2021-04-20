package it.unibo.actor0

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import java.io.File

object sysUtil{
	val userDirectory          = System.getProperty("user.dir")
	val cpus                   = Runtime.getRuntime().availableProcessors()
	var trace   : Boolean = false
	var logMsgs : Boolean = false


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	val singleThreadContext    = newSingleThreadContext("singleThread")
	val ioBoundThreadContext   = newFixedThreadPoolContext(64, "pool64")
	val cpusThreadContext      = newFixedThreadPoolContext(cpus, "cpuspool")
	val userThreadContext      = newFixedThreadPoolContext(2, "userpool") //@ObsoleteCoroutinesApi
	val dispatecherIO          = Dispatchers.IO
 	fun traceprintln( msg : String ){
		if( trace ) println( msg  )
	}
	
/*
 	MSG LOGS
*/ 	
	fun createFile( fname : String, dir : String = "logs" ){
 		val logDirectory = File("$userDirectory/$dir")
		logDirectory.mkdirs()	//have the object build the directory structure, if needed
		var file = File(logDirectory, fname)
//		println("               %%% sysUtil | createFile file $file in $dir")
		file.writeText("")	//file is created and nothing is written to it
	}
	
	fun deleteFile( fname : String, dir  : String ){
		File("$userDirectory/$dir/$fname").delete()
	}
	fun updateLogfile( fname: String, msg : String, dir : String = "logs" ){
		if( logMsgs ) File("$userDirectory/$dir/$fname").appendText("${msg}\n")
	}
//-----------------------------------------
	fun colorPrint(msg : String, color : Color = Color.LIGHT_CYAN ){
		println(Kolor.foreground("      $msg", color ) )
	}
	fun colorPrintNoTab(msg : String, color : Color = Color.BLUE ){
		println(Kolor.foreground("$msg", color ) )
	}

//-----------------------------------------
	@JvmStatic
	fun getCurrentTime():Long {
		return System.currentTimeMillis()
	}
	@JvmStatic
	fun getDuration(start: Long) : Long{
		val duration = (System.currentTimeMillis() - start)
		//println("DURATION = $duration start=$start")
		return duration
	}

	@JvmStatic
	fun aboutThreads(name: String): String =
			"-%- $name in curThread:${Thread.currentThread().name} | nthreads= ${Thread.activeCount()} cpus=$cpus"

	@JvmStatic
	fun aboutSystem(name: String): Long {
		println(aboutThreads(name))
		return getCurrentTime();
	}

	@JvmStatic
	fun showAliveThreads(){
		println("============= Alive threads ================== ")
		Thread.getAllStackTraces().keys.stream()
			.filter(Thread::isAlive)
			.forEach(System.out::println)
	}
//--------------------------------------------------------------------

	val connActive         : MutableSet<IConnInteraction> = mutableSetOf<IConnInteraction>()
	val connActiveForActor : MutableMap<String,IConnInteraction> = mutableMapOf<String,IConnInteraction>()


	fun curThread() : String {
		val nt = Thread.activeCount()
		return "thread=${Thread.currentThread().name} / nthreads=${nt}  "
	}
}//sysUtil
