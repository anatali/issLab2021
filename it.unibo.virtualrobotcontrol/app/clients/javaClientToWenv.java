/*
javaClientToWenv
https://zetcode.com/java/getpostrequest/
*/
package clients;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class javaClientToWenv{
    HttpClient client = HttpClient.newHttpClient();
    String URL   = 'http://localhost:8090/post' ;   // /api/move

    public void doJob(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .build();
        client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();

    }

    public static void main(String args[]){
    }
}

