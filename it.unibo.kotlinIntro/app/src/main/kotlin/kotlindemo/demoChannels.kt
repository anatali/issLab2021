package kotlindemo
//demoChannels.kt
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.CoroutineScope


suspend fun channelTest( scope : CoroutineScope ){
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

}

fun main() {
    println("BEGINS CPU=$cpus ${curThread()}")
    runBlocking {
        channelTest(this)
        println("ENDS runBockig ${curThread()}")
     }
    println("ENDS main ${curThread()}")
}