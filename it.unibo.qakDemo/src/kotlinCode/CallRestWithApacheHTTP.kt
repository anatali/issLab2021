package kotlinCode
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.commons.io.IOUtils

object CallRestWithApacheHTTP {
    val addr = "localhost:8081" ///moverest?move=$move" added by the caller

    fun doMove(moveTodo: String) : String {
        //try {
            val strUrl = "http://localhost:8081/moverest?move=$moveTodo"
            val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)
            val response: HttpResponse = client.execute(request)
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE=$answer")
            return answer
            //val obj =  JSONObject(json);
        //} catch (ex: Exception) { println(ex.message) }
    }

    fun doPath(pathTodo: String) : String{
        //try {
            val strUrl = "http://localhost:8081/dopath?path=$pathTodo"
            val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)
            val response: HttpResponse = client.execute(request)
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE=$answer")
            //val obj =  JSONObject(json);
            return answer
        //} catch (ex: Exception) { println(ex.message) }
    }
 
}//Object
