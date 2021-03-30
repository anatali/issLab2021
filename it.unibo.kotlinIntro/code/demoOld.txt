package kotlindemo

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine
 
val name = "Bob"
val str = "Hello $name"
val cpus = Runtime.getRuntime().availableProcessors();
		
fun curThread() : String { 
	return "thread=${Thread.currentThread().name} / nthreads=${Thread.activeCount()}" 
}

fun runBlockThread(){	    
 run { //Calls a function block; returns its result
    println("Out start: ${curThread()}")
    Thread.sleep(1500) 
    println("Out ended: ${curThread()}")
 }
}

suspend fun ioBoundFun(){
	val timeElapsed = measureTimeMillis {
		println("IO operation | STARTS in ${curThread()}")
		delay(1000)
	}
	println("IO operation | Done, TIME=$timeElapsed")
}
	

suspend fun activate(){
    val job1 = GlobalScope.async{
        ioBoundFun()
    }
    val job2 = GlobalScope.async{
        ioBoundFun()
    }
    if(! job1.isCompleted || ! job2.isCompleted)
    println("Waiting for completion")
    val end1 = job1.await()
    val end2 = job2.await()
    println("All jobs done")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun testDispatchers() {
runBlocking {
   launch { //context of the parent runBlocking  
   println("1) runBlocking | ${curThread()}")
 }
 launch( Dispatchers.Unconfined) { //in main thread  
   println("2) Unconfined | ${curThread()}")
 }
 launch( Dispatchers.Default) { //DefaultDispatcher  
   println("3) Default | ${curThread()}")
 }
 launch( newSingleThreadContext("MyThr")) { //new thread  
   println("4) newSingleThreadContext | ${curThread()}")
 }
}
}

suspend fun actionWithContext( n: Int){
 withContext(Dispatchers.Default) {
   println("$n) thread=${Thread.currentThread().name}")  
   delay(1000)
   println("ActionWithContext $n done")
 }
}

var counter = 0
//var counter = java.util.concurrent.atomic.AtomicInteger()

suspend fun CoroutineScope.massiveRun( action: suspend () -> Unit ) {
    val n=100		//number of coroutines to launch
    val k=1000		//times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch { repeat(k) { action() }  }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")    
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun runMassiveWithIncConfined(){
  val counterContext=newSingleThreadContext("CC")
  GlobalScope.massiveRun {
   //run each coroutine with DefaultDispathcer 
   withContext(counterContext) {
      //confine each increment to single-threaded context
      counter++	
   }
 }
}

suspend fun runMassiveWithMutex(){
val mutex = kotlinx.coroutines.sync.Mutex()
	GlobalScope.massiveRun{ 
		mutex.withLock{counter++}}	
}



suspend fun channelTest(){
 val timeElapsed = measureTimeMillis {
     val n = 5
     val channel = Channel<Int>(2)

     val sender = GlobalScope.launch {
         repeat( n ) {
             channel.send( it )
             println("SENDER | sent $it in ${curThread()}")
         }
     }
     delay(500) //The receiver starts after a while ...
     val receiver = GlobalScope.launch {
         for( i in 1..n ) {
             val v = channel.receive()
             println("RECEIVER | receives $v in ${curThread()}")
         }
     }

     delay(3000)
 }
 println("Done. time=$timeElapsed")
}


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    //val cpus = Runtime.getRuntime().availableProcessors();
    println("BEGINS CPU=$cpus ${curThread()}")
	
	//runBlockThread()
	
	//GlobalScope.launch{ runBlockThread() }
	
	//launch{ runBlockThread() }
	
//    val job =  launch{ runBlockThread()  }
//	job.join()
 
//	launch{ ioBoundFun() }
	
//	activate()
	
	//testDispatchers()
	
//	for(i in 1..3) launch{ actionWithContext(i) }
	
	//GlobalScope.massiveRun { counter++ }
	//GlobalScope.massiveRun{counter.incrementAndGet()}
	
	//runMassiveWithIncConfined()
	
	//runMassiveWithMutex()
	
	channelTest()
	
	//println("BYE with Counter = $counter")
	println("BYE")
	
	//Thread.sleep(1600) //Required to see the output
	
    println("ENDS ${curThread()}")
}