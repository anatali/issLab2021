package clients


import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.function.Consumer


@Service
object ClientWithWebClient {

    //val webClient2:WebClient = WebClient.create("http://localhost:8081")
    val webClient = WebClient.builder().build()

    fun doGet() {
        //val webClient = WebClient.builder().build()
        val result = webClient.get()
            .uri("localhost:8081")
            .retrieve()
            .bodyToFlux<String>(String::class.java) //Since we receive a string
        sysUtil.colorPrint("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG")
        sysUtil.colorPrint(result.toString())
        //String response = result.block( Duration.ofMillis(2000));
         result.subscribe(
            { item: String -> sysUtil.colorPrint("result item=  $item", Color.GREEN) },
            { error: Throwable -> println("result error= $error") },
            { println("result done ") }
        )
    }

    suspend fun doPost() {
        var resJson = ""
        val result = webClient.post()
            .uri("http://localhost:8081/moverest?move=l")
            //.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body<String, Mono<String>>(Mono.just<String>(resJson), String::class.java) //Since we send a string
            //.bodyToMono(String::class.java)
            .retrieve()
            .bodyToFlux<String>(String::class.java) //Since we receive a string
        sysUtil.colorPrint("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP")
        sysUtil.colorPrint(result.toString())
        result.subscribe(
            { item: String -> sysUtil.colorPrint("result item=  $item", Color.GREEN) },
            { error: Throwable -> sysUtil.colorPrint("result error= $error", Color.RED) },
            { sysUtil.colorPrint("result completed ", Color.GREEN) }
        )
        val response = result.blockLast(Duration.ofMillis(2000))
        sysUtil.colorPrint("result response=  $response", Color.GREEN)
    }
}

fun main( ) = runBlocking {
    //ClientWithWebClient.doGet()
    ClientWithWebClient.doPost()
    //delay(1000)
    sysUtil.colorPrint("BYE", Color.GREEN)
}