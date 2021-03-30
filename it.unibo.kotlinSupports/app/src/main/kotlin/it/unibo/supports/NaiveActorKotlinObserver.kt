package it.unibo.supports
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import org.json.JSONObject

class NaiveActorKotlinObserver(name: String, private val count: Int) : ActorBasicKotlin(name) {
/*
    protected fun handleInput(info: String) {
        //ActorBasicJava.delay(count*1000);
        println("$name  | $info   ${ aboutThreads()}" )
    }
*/
    override protected suspend fun handleInput(info : ApplMessage){
        var msgJsonStr = info.msgContent
        if( msgJsonStr.contains("@") ) msgJsonStr=msgJsonStr.replace("@",",")
        println("$name  | appl $info   ${ aboutThreads()}" )
        println("$name  | msgJsonStr=$msgJsonStr   " )
        val msgJson = JSONObject(msgJsonStr)
        println("$name  | msgJson=$msgJson   " )
    }
}