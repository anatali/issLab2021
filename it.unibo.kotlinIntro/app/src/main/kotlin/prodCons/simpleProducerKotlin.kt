package prodCons
//simpleProducerKotlin.kt
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

/*
produce coroutine builder : can send items to a ReceiveChannel
    - dispatcher : single or Dispatchers.IO
    - capacity   : default (0) or set by the user
 */

val dispatcher  = newSingleThreadContext("myThread") //Dispatchers.IO //
var simpleProducer : ReceiveChannel<Int>? = null

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun startProducer( scope: CoroutineScope ) {
    simpleProducer = scope.produce(dispatcher, capacity=2){ //capacity=1
        for (i in 1..4) {
            println("producer PRE -> $i  in  ${curThread()}  ")
            send(i)
            println("producer POST-> $i at ${System.currentTimeMillis()}   ")
            //delay( 500)
        }//for
    }
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun consume( ){
    val v = simpleProducer!!.receive()  //the first
    println( "consume- first ${v} at ${System.currentTimeMillis()} in ${curThread()}" )
    simpleProducer!!.consumeEach {
        println( "consume- $it at ${System.currentTimeMillis()} in ${curThread()}" )
    }
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() {
    println("BEGINS CPU=$cpus ${kotlindemo.curThread()}")
    runBlocking {
        startProducer(this)
        consume()
        println("ENDS runBlocking ${kotlindemo.curThread()}")
    }
    println("ENDS main ${kotlindemo.curThread()}")
}
