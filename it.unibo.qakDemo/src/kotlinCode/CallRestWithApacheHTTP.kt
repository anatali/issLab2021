package kotlinCode
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.commons.io.IOUtils
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.runBlocking

object CallRestWithApacheHTTP {
    val addr = "localhost:8081" ///moverest?move=$move" added by the caller
    val client: HttpClient = HttpClientBuilder.create().build()
    
	
	fun doMove(moveTodo: String) : String {
        //try {
            val strUrl = "http://localhost:8081/moverest?move=$moveTodo"
            //val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)
            val response: HttpResponse = client.execute(request)
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("CallRestWithApacheHTTP | RESPONSE for $moveTodo=$answer")
            return answer
            //val obj =  JSONObject(json);
        //} catch (ex: Exception) { println(ex.message) }
    }

    fun doPath(pathTodo: String) : String{
        //try {
            val strUrl = "http://localhost:8081/dopath?path=$pathTodo"
            //val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)
		    println("doPath $pathTodo url=$strUrl")
            val response: HttpResponse = client.execute(request)
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("CallRestWithApacheHTTP | RESPONSE for pathTodo=$answer")
            //val obj =  JSONObject(json);
            return answer
        //} catch (ex: Exception) { println(ex.message) }
    }
 
}//Object

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        //val result = CallRestWithApacheHTTP.doMove("p")
        CallRestWithApacheHTTP.doMove("w")
		CallRestWithApacheHTTP.doMove("w")
		CallRestWithApacheHTTP.doMove("l")
		CallRestWithApacheHTTP.doMove("w")
/*
        val importantPathToCheck = "wwlw"
        val result = CallRestWithApacheHTTP.doPath(importantPathToCheck)
        println("result=$result  ")
 */
        println("ENDS runBlocking ${sysUtil.curThread()}")
    }
    println("ENDS main ${sysUtil.curThread()}")
}
