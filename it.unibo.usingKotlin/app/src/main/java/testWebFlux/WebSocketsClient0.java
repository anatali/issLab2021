/*
==============================================================
WebSocketsClient0
==============================================================
With Flux I'll receive the answer to an HTTP request (GET)
as a stream of data
 */

package testWebFlux;

import java.time.Duration;
import java.util.List;

/*
 * From https://www.baeldung.com/spring-log-webclient-calls
 * Behind the scenes, WebClient calls an HTTP client. 
 * Reactor Netty is the default and reactive HttpClient of Jetty is also supported. 
 * Moreover, it's possible to plug other implementations of HTTP client by setting up a ClientConnector for WebClient.
 */
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
public class WebSocketsClient0 {
	/*
	public void callForHomePage() {
		WebClient webClient = WebClient.create("http://localhost:8090/api/move");
		Mono<String> result = callForUri(webClient, "/" );
		System.out.println(" %%%%%%%%%%%% callForHomePage");
		String outS = result.block();
		System.out.println(outS); 
	}
	
	private Mono<String> callForUri( WebClient webClient, String uripath ){
		Mono<String> result = webClient.get()
		        .uri( uripath )
		        .retrieve()
		        //.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus), clientResponse -> Mono.empty())
		        .bodyToMono(String.class);
		return result;
	}

	public void getListFromFlux() {
		WebClient webClient = WebClient.create("http://localhost:8090");
        List<String> result = webClient.get()
				.uri("/api/move")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();     
        System.out.println( "getListFromFlux ..." );
        result.forEach( v ->  System.out.println( v ) );  
 	}	
*/


	public void callForFlux() {
		WebClient webClient = WebClient.create("http://localhost:8090");

        Flux<String> result = webClient.get()
				 .uri("/")	//api/move
                .retrieve()
                .bodyToFlux(String.class);
        result.subscribe(  
        		item  -> System.out.println("item=  " + item),
        		error -> System.out.println("error= " + error ),
        		()    -> System.out.println("done " )   
        );
        //Block to avoid premature termination
        //String last = result.blockLast( Duration.ofMillis(10000));
        //System.out.println("WebClientExample | callForFlux last= " + last);
        //webClient.delete();
	}	

 
    public static void main(String[] args)   {   	
    	WebSocketsClient0 appl = new WebSocketsClient0();
    	//appl.callForHomePage();
    	//appl.getListFromFlux();
    	appl.callForFlux();
    	System.out.println( "going to sleep ..." );
    	try { Thread.sleep(35000); } catch (InterruptedException e) {}
     }
}