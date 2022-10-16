package unibo.SpringDataRest.callers;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.Person;

public class RestTemplateApiCaller {
     final protected String BASE_URL     = "http://localhost:8080/Api";

/*
------------------------------------------
Operazioni che usano BASE_URL
------------------------------------------
 */
    public void runGetLastPerson( ){
        System.out.println("--------- runGetLastPerson");
        String response =  doGet(BASE_URL +"/");
        //System.out.println(response);   //Visualizza la pagina
        PageUtil.readTheHtmlPage(response,"LASTPERSON");
    }
     public void runGet(String lastName){
        System.out.println("--------- runGet");
        String response =  doGet(BASE_URL +"/getAPerson?lastName="+lastName);
        //System.out.println(response);   //Visualizza la pagina
         PageUtil.readTheHtmlPage(response,"FOUND");
    }
    public void runGetAll( ){
        System.out.println("--------- runGetAll");
        String response =  doGet(BASE_URL +"/getAllPersons");
        //System.out.println(response);   //Visualizza la pagina
        PageUtil.readTheHtmlPage(response,"ALLPERSONS");
    }

    protected String doGet(String url)  {
        try{
            RestTemplate rt = new RestTemplate( );
            ResponseEntity<String> response = rt.getForEntity( url, String.class);
            //System.out.println("response:" + response );
            //System.out.println("response body:" + response.getBody() );
            return response.getBody().toString();
        }catch(Exception e){
            return "error: " +e.getMessage();
        }
    }
    public void runPost() {
        System.out.println("--------- runPost");
        String url = BASE_URL +"/createPerson";
        HttpHeaders headers = new HttpHeaders();
        //String requestJson = "{\"aaa\":\"bbb\"}";
        //headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String requestData  = "id=2&firstName=Giovanni&lastName=Verga";
        HttpEntity<String> entity = new HttpEntity<String>(requestData,headers);
        doPost(url,entity);
     }

    protected void doPost(String urlStr, HttpEntity<String> entity)  {
        try{
            RestTemplate rt = new RestTemplate( );
            String answer   = rt.postForObject(urlStr, entity, String.class);
            System.out.println(answer);
        }catch(Exception e){
            System.out.println("response.ERROR =" + e.getMessage() );
        }
    }



    public static void main(String[] args)  {
        RestTemplateApiCaller appl = new RestTemplateApiCaller();
        appl.runGetLastPerson();
        //appl.runGetAll();
        //appl.runPost();
      }
}
