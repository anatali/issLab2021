package unibo.SpringRestH2;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class DbJavaCaller {
    final OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch(Exception e){
            //e.printStackTrace();
            return "error";
        }

    }

    public static void main(String[] args) throws IOException {
        DbJavaCaller example = new DbJavaCaller();
        //String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        System.out.println("--------- TUTTI I PRODOTTI");
        String response = example.run("http://localhost:8080/products");
        System.out.println(response);
        System.out.println("--------- PRIMO PRODOTTO");
        response = example.run("http://localhost:8080/products/1");
        System.out.println(response);
    }
}
