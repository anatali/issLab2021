package unibo.SpringRestH2;

import com.squareup.okhttp.*;


public class DbJavaCaller {
    final OkHttpClient client = new OkHttpClient();
    final String BASE_URL     = "http://localhost:8080";

    public void testGet(){
        System.out.println("--------- TUTTI I PRODOTTI");
        String response =  doGet("http://localhost:8080/products");
        System.out.println(response);
        System.out.println("--------- PRIMO PRODOTTO");
        response =  doGet("http://localhost:8080/products/1");
        System.out.println(response);
    }

    public void testPost() {
        String json =
        "{\"category\": \"category/2\",\"name\": \"box\",\"code\": \"004\",\"title\": \"box\", \"description\": \"new box\", \"imgUrl\": \"\", \"price\": 35}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        String response = doPost(BASE_URL + "/products",body);
        System.out.println("testPost response="+response);
    }
    String doPost(String urlStr, RequestBody body)  {
        try{
            Request request = new Request.Builder()
                .url(urlStr)
                    //.addHeader("Authorization", Credentials.basic("username", "password"))
                .post(body)
                .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            return response.body().string();
        }catch(Exception e){
            return "error: " +e.getMessage();
        }
    }

    String doGet(String url)  {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch(Exception e){
            return "error: " +e.getMessage();
        }
    }

    public static void main(String[] args)  {
        DbJavaCaller appl = new DbJavaCaller();
        //String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        appl.testGet();
        //appl.testPost();
      }
}
