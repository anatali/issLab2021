package kotlindemo
//demoCoroutinesIntro.kt
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.Job
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.Executors

 
//import kotlinx.coroutines.io.parallelis.IO_PARALLELISM_PROPERTY_NAME
var thcounter=0
val delayTime=0L
//val dispatcher = Executors.newFixedThreadPool(128).asCoroutineDispatcher()

fun runBlockThread( delay : Long = 0L ){
//     run { //Calls a function block; returns its result
//       println("thread sleeps ... : ${curThread()}")
         Thread.sleep( delay )
         thcounter++
         println("thread ends : ${curThread()} thcounter=${thcounter}")
//     } 
}

fun scopeDemo (){
	thcounter=0
	val scope = CoroutineScope( Dispatchers.Default )
	println( scope.coroutineContext )
	scope.launch{ runBlockThread(1500) } 	
}
 

fun scopeAsyncDemo (){
	val scope = CoroutineScope( Dispatchers.Default )
	
	val res : Deferred<String>   = scope.async{
		println("async starts")
		Thread.sleep(3000)
		println("async ends")
		"hello from async"
	}
							 
	scope.launch{
		println("result waiting starts")
		val r = res.await(); //must be called only from a coroutine or a suspend function	
		println("result waiting result= ${r}")
	}
}

val n=10000		//number of Thread or Coroutines to launch
val k=1000		//times an action is repeated by each Thread or Coroutine

fun manyThreads(){  
 	thcounter=0
 	val time = measureTimeMillis{
		val jobs = List(n){
			kotlin.concurrent.thread(start = true) { 
  				repeat( k ){ runBlockThread() }
			}
		}			
		jobs.forEach{ it.join()  }  //wait for termination of all threads
 	}
  	println("manyThreads time= $time thcounter=$thcounter ")
}

//Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()

fun manyCoroutines(){
	//val d = newFixedThreadPoolContext(650,"d")
	val d = Dispatchers.Default 
	val scope = CoroutineScope( d  ) 
	thcounter=0
 	scope.launch{ 
	    val time = measureTimeMillis {
 	        val jobs = List(n) {
				//println("coroutine ${iter++} starts ")
 				scope.launch { repeat(k) { runBlockThread() } }
 			} 
			//println("manyCoroutines ENDS LANUCH ")
			jobs.forEach { it.join() }
			//println("manyCoroutines END ALL JOBS")
	    }
	    println("manyCoroutines time= $time  thcounter=$thcounter  ")
	}
}

//=================================================================
var demoTodo : () -> Unit = { println("nothing to do") }

fun readInt() : Int { print(">"); return readLine()!!.toInt() }

fun doDemo( input : Int ) = runBlocking{  
	println("BEGINS CPU=$cpus ${curThread()}")
	when( input ){
		1 ->  demoTodo =  { runBlockThread(1500 )                      	}
		2 ->  demoTodo =  { thcounter=0; GlobalScope.launch{ runBlockThread(1500) }	}
		3 ->  demoTodo =  { scopeDemo()								}
 		4 ->  demoTodo =  { scopeAsyncDemo()             	        }
 		5 ->  demoTodo =  { manyThreads()                	        }
  		6 ->  demoTodo =  { manyCoroutines()               	        }
		else ->  { println("command unknown") }  //Note the block  
	} 			
	println( "work done in time= ${measureTimeMillis(  demoTodo )}"  )
	println("ENDS ${curThread()}")	
}

fun main() {
		var input =  readInt()
		while( input != 0 ){
			doDemo( input )
			demoTodo = 	{ println("nothing to do") }    
			input    =  readInt()
		}
  	    println( "BYE") 
}

     
