package utils

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

  
object sonarDataSimulator {
var dataCounter = 1	
    fun sonarValFromUser( result : MutableMap<String,String> , key : String  ){
		print("\nsonarValFromUser>" )
		runBlocking{
			val job = GlobalScope.launch{	//TO AVOID BLOCKING OF OTHER ACTORS IN THE CONTEXT
		    	var data = readLine()
		        //println("data ${dataCounter++} = $data " )
				result.put( key, "$data" )
			}
			job.join()
		}
     }
	
	/*
 WARNING: it could block other actors, since blocks a thread
    */
	fun  getSonarValBlocking() : String{
		print("\nsonarValBlocking>" )
		var data = readLine()
		println("getSonarValBlocking value=$data " )
		return data!!
	}

	suspend fun  getSonarVal() : String{
		print("\nsonarValFromUser>" )
		var data : String? = null
		val job = GlobalScope.launch{	//TO AVOID BLOCKING OF OTHER ACTORS IN THE CONTEXT
		    data = readLine()
		    println("getSonarVal value=$data " )
 		}
		job.join()
		return data!!
	}

}
 