package unibo.SpringDataRest.callers;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestTemplateApiUtil {
     protected String BASE_URL ;

     public RestTemplateApiUtil(String BASE_URL){
         this.BASE_URL = BASE_URL;
     }
/*
------------------------------------------
GET
------------------------------------------
 */
    public ResponseEntity<String> getLastPerson( ){
         return  doGet(BASE_URL +"/");
    }
     public ResponseEntity<String> getAPerson(String lastName){
          return  doGet(BASE_URL +"/getAPerson?lastName="+lastName);
    }
    public ResponseEntity<String> getAllPersons( ){
         return  doGet(BASE_URL +"/getAllPersons");
    }

    protected ResponseEntity<String> doGet(String url)  {
        //url=http://localhost:8080/Api/               per runGetLastPerson
        //url=http://localhost:8080/Api//getAllPersons per runGetLastPerson
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt.getForEntity( url, String.class);
            //response:<200, HTMLPAGE,[Content-Type:"text/html;charset=UTF-8", ...]>
            //response.getStatusCode: 200 OK
            //System.out.println("    response:" + response.getStatusCode() );
            //System.out.println("    response body:" + response.getBody() );
        return response;
    }

/*
------------------------------------------
POST
------------------------------------------
*/
    public ResponseEntity<String> createPerson(String id, String firstName, String lastName) {
        //System.out.println("--------- createPerson");
        String url = BASE_URL +"/createPerson";
        HttpHeaders headers = new HttpHeaders();
        //String requestJson = "{\"aaa\":\"bbb\"}";
        //headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String personData  = "id=ID&firstName=FN&lastName=LN".replace("ID",id)
                .replace("FN",firstName).replace("LN",lastName);
        HttpEntity<String> entity = new HttpEntity<String>(personData,headers);
        return doPost(url,entity);
     }

    protected ResponseEntity<String> doPost(String urlStr, HttpEntity<String> entity)  {
        RestTemplate rt = new RestTemplate( );
         ResponseEntity<String> response = rt
                .exchange(urlStr, HttpMethod.POST, entity, String.class);
        return response;
    }

/*
------------------------------------------
DELETE
------------------------------------------
*/
    public ResponseEntity<String> deletePerson(String id, String firstName, String lastName) {
        //System.out.println("--------- deletePerson");
        String url = BASE_URL +"/deletePerson";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String personData  = "id=ID&firstName=FN&lastName=LN".replace("ID",id)
                .replace("FN",firstName).replace("LN",lastName);
        HttpEntity<String> entity = new HttpEntity<String>(personData,headers);
        return doDelete(url,entity);
    }
    protected ResponseEntity<String> doDelete(String urlStr, HttpEntity<String> entity)  {
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt
                .exchange(urlStr, HttpMethod.DELETE, entity, String.class);
        return response;
    }


    public static void main(String[] args)  {
        RestTemplateApiUtil appl = new RestTemplateApiUtil("http://localhost:8080/Api");
        appl.getAllPersons();
        //appl.runGetAll();
        //appl.runPost();
      }
}
