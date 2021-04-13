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
fun testDispatchers(n : Int, scope: CoroutineScope) {
        if( n== 1 ){
            runBlocking {
                 launch { //context of the parent runBlocking
                     delay(500)
                     println("1_a) runBlocking | ${curThread()}")
                 }
                launch { //context of the parent runBlocking
                    println("1_b) runBlocking | ${curThread()}")
                }
            }
        }
        if( n== 2 ) {
            val dispatcher = Dispatchers.Default
            scope.launch(dispatcher) {
                delay(500)
                println("2_a) Default | ${curThread()}")
            }
            scope.launch(dispatcher) { //DefaultDispatcher
                println("2_b) Default | ${curThread()}")
            }
        }
        if( n== 3 ){
            val dispatcher = newSingleThreadContext("MyThr")
            scope.launch( dispatcher ) {
                delay(500)
                 println("3-a) newSingleThreadContext | ${curThread()}")
            }
            scope.launch( dispatcher ) {
                println("3-b) newSingleThreadContext | ${curThread()}")
            }
        }
        if( n== 4 ) {
            val dispatcher = Dispatchers.IO
            scope.launch(dispatcher) {
                delay(500)
                println("4_a) Dispatchers.IO | ${curThread()}")
            }
            scope.launch(dispatcher) {
                println("4_b) Dispatchers.IO | ${curThread()}")
            }
        }
         if( n== 5 ) {
            val dispatcher = Dispatchers.Unconfined
            scope.launch(dispatcher) {
                delay(500)
                println("5_a) Unconfined | ${curThread()}")
            }
            scope.launch(dispatcher) {
                println("5_b) Unconfined | ${curThread()}")
            }
        }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
    testDispatchers(1, this)
    //testDispatchers(2, this)
    //testDispatchers(3, this)
    //testDispatchers(4, this)
    //testDispatchers(5, this)
    println("ENDS ${curThread()}")
}