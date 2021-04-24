package it.unibo.webspring.intro; 
 
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
 

public class AClient {

	public static void main(String[] args)  {
        try {
            String strUrl = "http://localhost:8080/model";
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(strUrl);
            HttpResponse response = client.execute(request);
//            System.out.println( "RESPONSE=" + response.getEntity().getContent());
            String answer = IOUtils.toString(response.getEntity().getContent(), "UTf-8");
            System.out.println( "RESPONSE=" + answer );
//            JSONObject obj = new JSONObject(json);
//            System.out.println(obj.get("url"));
        } catch ( Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

