package clients

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestTemplate
import java.util.HashMap

object ClientWithRestTemplate {
    //@Autowired
    private lateinit var restTemplate: RestTemplate

    fun doGet(){
        restTemplate = RestTemplate()
        val uri = "http://localhost:8081"
        val result = restTemplate.getForObject<String>(uri, String::class.java)
        println(result)
    }

    fun doPostWithParams(){
        var answer = ""
        val params: MutableMap<String, String> = HashMap()
        params["MYMOVE"] = "r"
        restTemplate = RestTemplate()
        val uri = "http://localhost:8081/moverest?move={MYMOVE}"   //?idparam={idparam}
        val result = restTemplate.postForObject(uri, answer, String::class.java, params)
        println(result)
    }
    fun doPostSimple(moveTodo: String){
        var answer = ""
        restTemplate = RestTemplate()
        val uri = "http://localhost:8081/moverest?move=$moveTodo"
        val result = restTemplate.postForObject(uri, answer, String::class.java )
        println(result)
    }
}

fun main(args: Array<String>) {
    //ClientWithRestTemplate.doGet()
    ClientWithRestTemplate.doPostSimple("l")
    //ClientWithRestTemplate.doPostSimple("r")
    ClientWithRestTemplate.doPostSimple("w")
    //ClientWithRestTemplate.doPostWithParams()
}