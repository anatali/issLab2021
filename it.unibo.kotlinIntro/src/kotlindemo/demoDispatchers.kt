package kotlindemo
//demoDispatchers
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

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
    testDispatchers()
    println("ENDS ${curThread()}")
}