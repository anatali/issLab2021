package unibo.SpringDataRest.callers;
import com.squareup.okhttp.*;


public class DataHIHttpCaller {
    final private OkHttpClient client = new OkHttpClient();
    final protected String BASE_URL   = "http://localhost:8080";

    public void runGet(String lastName){
        System.out.println("--------- runGet");
        String response =  doGet(BASE_URL +"/Api/getAPerson?lastName="+lastName);
        //System.out.println(response);   //Visualizza la pagina: prolisso
        //Visualizzimamo l'elemento della pagina che contiene la risposta
        PageUtil.readTheHtmlPage(response,"FOUND");
    }
    public void runGetAll( ){
        System.out.println("--------- runGetAll");
        String response =  doGet(BASE_URL +"/Api/getAllPersons");
        //System.out.println(response);   //Visualizza la pagina: prolisso
        //Visualizzimamo l'elemento della pagina che contiene la risposta
        PageUtil.readTheHtmlPage(response,"ALLPERSONS");
    }

    //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
    public void runCreate(String id, String firstName, String lastName) {
        //String json = "{\"id\": \"1\",\"firstName\": \"...\",\"lastName\": \"...\"}";
        //RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        String personData  = "id=ID&firstName=FN&lastName=LN".replace("ID",id)
                  .replace("FN",firstName).replace("LN",lastName);
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), personData);
        //System.out.println("runCreate doPost="+BASE_URL + "/createPerson");
        //String response = doPost( "http://localhost:8080/Api/createPersonWithModel", body);  //OK
        int respCode = doPost( BASE_URL +"/Api/createPerson", body);                      //OK
        //System.out.println("runCreate response="+response);
        if( respCode == 200 || respCode == 201) System.out.println("runCreate ok" );
        else System.out.println("WARNING: runCreate problem:" + respCode);


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
        DataHIHttpCaller appl = new DataHIHttpCaller();
        appl.runGetAll();
        appl.runGet("Foscolo");  //person not found
        appl.runCreate("2","Ugo","Foscolo");
        appl.runGet("Foscolo");
      }
}
/*
From https://www.geeksforgeeks.org/java-program-to-extract-content-from-a-html-document/
 */