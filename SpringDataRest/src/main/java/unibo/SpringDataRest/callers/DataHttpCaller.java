package unibo.SpringDataRest.callers;
import com.squareup.okhttp.*;

import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.StringReader;


public class DataHttpCaller {
    final private OkHttpClient client = new OkHttpClient();
    final protected String BASE_URL   = "http://localhost:8080";

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
            System.out.println("foundField:"  + start + " " + length);
            String s   = foundField.getDocument().getText(start,length);
            System.out.println( s );
            //System.out.println("title="+htmlDocument.getProperty("title")); //

        } catch( Exception e){
            System.out.println( "readTheHtmlPage ERROR:"+e.getMessage());
             //e.printStackTrace();
        }
    }
    public void runGet(String lastName){
        System.out.println("--------- runGet");
        String response =  doGet(BASE_URL +"/Api/getAPerson?lastName="+lastName);
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"FOUND");
    }
    public void runGetAll( ){
        System.out.println("--------- runGetAll");
        String response =  doGet(BASE_URL +"/Api/getAllPersons");
        //System.out.println(response);   //Visualizza la pagina
        readTheHtmlPage(response,"ALLPERSONS");
    }

    //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
    public void runPost() {
        //String json = "{\"id\": \"1\",\"firstName\": \"Ugo\",\"lastName\": \"Foscolo\"}";
        //RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        String personData  = "id=1&firstName=Ugo&lastName=Foscolo";
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), personData);
        //System.out.println("runPost doPost="+BASE_URL + "/createPerson");
        //String response = doPost( "http://localhost:8080/Api/createPersonWithModel", body);  //OK
        int respCode = doPost( BASE_URL +"/Api/createPerson", body);                      //OK
        //System.out.println("runPost response="+response);
        if( respCode == 200 || respCode == 201) System.out.println("runPost ok" );
        else System.out.println("WARNING: runPost problem:" + respCode);


    }
    protected int doPost(String urlStr, RequestBody body)  {
        try{
            Request request = new Request.Builder()
                .url(urlStr)
                //.addHeader("Authorization", Credentials.basic("username", "password"))
                .post(body)
                .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            //System.out.println("doPost response code="+response.code());
            return( response.code()   )  ;
        }catch(Exception e){
            return 0;
        }
    }

    protected String doGet(String url)  {
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
        appl.runGet("Foscolo");
        appl.runPost();
        appl.runGet("Foscolo");
      }
}
/*
From https://www.geeksforgeeks.org/java-program-to-extract-content-from-a-html-document/
 */