package prodCons

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope

var simpleProducer : ReceiveChannel<Int>? = null

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun startProducer(scope : CoroutineScope ){
	simpleProducer =
        scope.produce  {
            for( i in 1..3 ){
                send( i )
                println( "producer produced $i in  ${curThread()}")
            }
        }
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun consume(){
    val v = simpleProducer!!.receive()
    println( "consume receives ${v} in ${curThread()}" )
    simpleProducer!!.consumeEach {
        println( "consume receives $it in ${curThread()}" )
    }
}


@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${kotlindemo.curThread()}")
    println( this ) //BlockingCoroutine{Active}@799f7e29	
    startProducer(this);
	consume()
	
    println("ENDS ${kotlindemo.curThread()}")
}
