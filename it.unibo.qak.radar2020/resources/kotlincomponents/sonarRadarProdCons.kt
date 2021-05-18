package kotlincomponents
//sonarRadarProdCons.kt
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
 
fun curThread() : String { 
	return "thread=${Thread.currentThread().name}" 
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
val sonarDataProducer : ReceiveChannel<Int> =
  GlobalScope.produce {
    for( i in 1..3 ){
        println( "sonarDataProducer | produces $i in ${curThread()}")
        send( i )
    }
  }

//val radarGuiConsumer  = GlobalScope.actor<Int> {
//}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val radarGuiConsumer  = GlobalScope.launch {
    val v = sonarDataProducer.receive()
    println( "radarGuiConsumer  | receives ${v} in ${curThread()}" )
    	sonarDataProducer.consumeEach( {
       println( "radarGuiConsumer  | receives $it in ${curThread()}" )
    })
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("sonarRadarProdCons| BEGIN")
	radarGuiConsumer.join()
    println("sonarRadarProdCons| END")
}