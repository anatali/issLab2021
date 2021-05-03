package it.unibo.webspring.demo

import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import java.lang.Exception
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair
import org.apache.http.NameValuePair
import java.util.ArrayList

object AClientApacheHttp {

    fun doSimplePost(){
        try {
            val strUrl = "http://localhost:8081/moverest"
            val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)
            val response: HttpResponse = client.execute(request)
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE=$answer")
            //val obj =  JSONObject(json);
//            println(obj.get("url"));
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun doPostWithParams(){
        try {
            val strUrl = "http://localhost:8081/moverest"
            val client: HttpClient = HttpClientBuilder.create().build()
            val request = HttpPost(strUrl)

            val params: MutableList<NameValuePair> = ArrayList()
            params.add(BasicNameValuePair("move", "l"))
            request.setEntity(UrlEncodedFormEntity(params))

            val response: HttpResponse = client.execute(request)
            //            System.out.println( "RESPONSE=" + response.getEntity().getContent());
            val answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE=$answer")
            //val obj =  JSONObject(json);
//            println(obj.get("url"));
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //doSimplePost()
        doPostWithParams()
    }
}