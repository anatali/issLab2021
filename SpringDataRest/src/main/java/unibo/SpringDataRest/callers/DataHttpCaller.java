package unibo.SpringDataRest.callers;
import com.squareup.okhttp.*;

import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.StringReader;


public class DataHttpCaller {
    final OkHttpClient client = new OkHttpClient();
    final String BASE_URL     = "http://localhost:8080/Api";

     private void readTheHtmlPage(String htmlString, String elementID){
        try {
             HTMLEditorKit htmlEditKit = new HTMLEditorKit();
             HTMLDocument htmlDocument = new HTMLDocument();
             try {
                htmlEditKit.read(new StringReader( htmlString ), htmlDocument, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Element foundField  = htmlDocument.getElement(elementID);
            int start  = foundField.getStartOffset();
            int length = foundField.getEndOffset() - start;
            String s   = foundField.getDocument().getText(start,length);
            //System.out.println("foundField:"  + start + " " + length);
            System.out.println( s );
            //System.out.println("title="+htmlDocument.getProperty("title")); //

        } catch( Exception e){
             e.printStackTrace();
        }
    }
    public void runGet(String lastName){
        System.out.println("--------- runGet");
        String response =  doGet("http://localhost:8080/Api/getAPerson?lastName="+lastName);
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"FOUND");
    }
    public void runGetAll( ){
        System.out.println("--------- runGetAll");
        String response =  doGet("http://localhost:8080/Api/getAllPersons");
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"ALLPERSONS");
    }

    public void runPost() {
        String json = "{\"id\": \"1\",\"firstName\": \"Ugo\",\"lastName\": \"Foscolo\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        System.out.println("runPost doPost="+BASE_URL + "/createPerson");
        String response = doPost(BASE_URL + "/createPerson", body);
        System.out.println("runPost response="+response);
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
        DataHttpCaller appl = new DataHttpCaller();
        appl.runGetAll();
        appl.runGet("Manzoni");
        //appl.runPost();
      }
}
/*
From https://www.geeksforgeeks.org/java-program-to-extract-content-from-a-html-document/
 */