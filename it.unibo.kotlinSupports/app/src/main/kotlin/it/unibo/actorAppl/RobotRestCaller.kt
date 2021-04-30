package it.unibo.actorAppl

import org.springframework.web.client.RestTemplate
import java.util.HashMap

object RobotRestCaller {
    private lateinit var restTemplate: RestTemplate

    fun doGet(addr: String, arg: String=""){
        restTemplate = RestTemplate()
        val uri = "http://$addr"
        val result = restTemplate.getForObject<String>(uri, String::class.java)
        println(result)
    }


    fun doPostSimple( addr: String, move: String){  //Move = w | s | ...
        var answer = ""
        restTemplate = RestTemplate()
        val uri = "http://$addr/moverest?move=$move"
        val result = restTemplate.postForObject(uri, answer, String::class.java )
        println(result)
    }

}
fun main(args: Array<String>) {
    //ClientWithRestTemplate.doGet()
    RobotRestCaller.doPostSimple("localhost:8081","l")

}