package it.unibo.supports
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage

class NaiveActorKotlinObserver(name: String, private val count: Int) : ActorBasicKotlin(name) {

    protected fun handleInput(info: String) {
        //ActorBasicJava.delay(count*1000);
        println("$name  | $info   ${ aboutThreads()}" )
    }

    override protected suspend fun handleInput(info : ApplMessage){
        println("$name  | appl $info   ${ aboutThreads()}" )
    }
}