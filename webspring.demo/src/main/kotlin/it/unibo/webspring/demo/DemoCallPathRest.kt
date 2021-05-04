package it.unibo.webspring.demo

import it.unibo.actor0.sysUtil
import it.unibo.actorAppl.RobotRestCaller
import kotlinx.coroutines.runBlocking
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.commons.io.IOUtils
import java.lang.Exception

object DemoCallPathRest {
    val addr = "localhost:8081" ///moverest?move=$move" added by the caller

    fun doMoveApache(moveTodo: String){
        try {
            val strUrl = "http://localhost:8081/moverest?move=$moveTodo"
            val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)
            val response: HttpResponse = client.execute(request)
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE=$answer")
            //val obj =  JSONObject(json);
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun doPathApache(pathTodo: String){
        try {
            val strUrl = "http://localhost:8081/dopath?path=$pathTodo"
            val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)
            val response: HttpResponse = client.execute(request)
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE=$answer")
            //val obj =  JSONObject(json);
        } catch (ex: Exception) {
            println(ex.message)
        }

    }

    fun doMove(moveTodo: String=""){
        RobotRestCaller.doPostBasicmove(addr,moveTodo)
    }
}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        //DemoCallPathRest.doMove("r")
        //DemoCallPathRest.doMoveApache("l")
        DemoCallPathRest.doPathApache("rl")
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
