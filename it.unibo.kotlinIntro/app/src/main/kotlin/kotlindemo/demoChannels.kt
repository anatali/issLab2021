package kotlindemo
//demoChannels.kt
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


suspend fun channelTest( scope : CoroutineScope ){
//val df = Dispatchers.Default
val n = 5
val channel = Channel<Int>(2)
		println( channel )	//ArrayChannel capacity=2 size=0
	
        val sender = scope.launch {
            repeat( n ) {
                channel.send( it )
                println("SENDER | sent $it in ${curThread()}")
            }
        }
        
	delay(500) //The receiver starts after a while ...
        
		val receiver = scope.launch {
            for( i in 1..n ) {
                val v = channel.receive()
                println("RECEIVER | receives $v in ${curThread()}")
            }
        }

        //delay(1000)
//    }
//    println("Done. time=$timeElapsed")
}
//-------------------- PROD CONS ------------------------

fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
     channelTest( this )    		  //(1)
	
	//startProducer(this); consume()	  //(2)
           		   
    println("ENDS ${curThread()}")
}