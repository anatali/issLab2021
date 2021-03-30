package it.unibo.actor0

import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import java.io.File

/*
ECLIPSE KOTLIN
https://dl.bintray.com/jetbrains/kotlin/eclipse-plugin/last/
*/

//A module in kotlin is a set of Kotlin files compiled together
object sysUtil{
	val userDirectory          = System.getProperty("user.dir")
	val cpus                   = Runtime.getRuntime().availableProcessors()
	var trace   : Boolean = false
	var logMsgs : Boolean = false


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	val singleThreadContext    = newSingleThreadContext("singleThread")
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	val ioBoundThreadContext   = newFixedThreadPoolContext(64, "pool64")
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	val cpusThreadContext      = newFixedThreadPoolContext(cpus, "cpuspool")

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
			"-%- $name in ${Thread.currentThread().name} | nthreads= ${Thread.activeCount()} cpus=$cpus"

	@JvmStatic
	fun aboutSystem(name: String): Long {
		println(aboutThreads(name))
		return getCurrentTime();
	}


}//sysUtil
