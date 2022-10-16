package unibo.SpringDataRest.callers;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class RestTemplateRestApiCaller {
    final protected String RESTBASE_URL = "http://localhost:8080/RestApi";
/*
------------------------------------------
Operazioni che usano RestApi
------------------------------------------
*/
    //https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types

    public void runGetLastPerson(){
        System.out.println("--------- runGetLastPerson");
        try{
            String url = RESTBASE_URL +"/getLastPerson";
            RestTemplate rt = new RestTemplate( );
            //ResponseEntity<String> response = rt.getForEntity( url, String.class); // (1) String Json
            ResponseEntity<Person> response = rt.getForEntity( url, Person.class);   //(2) Da Json a Person
            System.out.println("response:" + response );
            System.out.println("response code:" + response.getStatusCode() );
            System.out.println("response body:" + response.getBody() );
            System.out.println("response body class:" + response.getBody().getClass() ); //(1)->String, (2)->Person
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void runGetAllPersons(){
        System.out.println("--------- runGetAllPersons");
        try{
            String url = RESTBASE_URL +"/getAllPersons";
            RestTemplate rt = new RestTemplate( );
            ResponseEntity<String> response = rt.getForEntity( url, String.class);
            System.out.println("response:" + response );
            System.out.println("response code:" + response.getStatusCode() );
            System.out.println("response body:" + response.getBody() );
            System.out.println("response body class:" + response.getBody().getClass() );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getAllPersonsList(){
        System.out.println("--------- runGetAllPersons");
        try{
            String url = RESTBASE_URL +"/getAllPersons";
            RestTemplate rt = new RestTemplate( );
            ResponseEntity<Object> response = rt.getForEntity( url, Object.class);
            System.out.println("response:" + response );
            System.out.println("response code:" + response.getStatusCode() );
            System.out.println("response code:" + response.getBody().getClass());
            ArrayList<LinkedHashMap> answer= (ArrayList<LinkedHashMap>) response.getBody();
            System.out.println("response body:" + answer );
            System.out.println("answer.get(0):" + answer.get(0).getClass()  );
            //LinkedHashMap<String,Object> lhmap = (LinkedHashMap<String, Object>) answer.get(0);
            //System.out.println("lhmap.keySet:" + lhmap.keySet()  );
            //System.out.println("lhmap.get:" + lhmap.get("firstName")  );
            Iterator< LinkedHashMap > iter = answer.iterator();
            while( iter.hasNext() ){
                LinkedHashMap<String,Object> v = iter.next();
                System.out.println("item ... "  );
                System.out.println("id:" + v.get("id")  ); //Integer
                System.out.println("firstName:" + v.get("firstName")  ); //String
                System.out.println("LastName:" + v.get("lastName") ); //String
            }
            System.out.println("ITEMS ..."  );
            answer.forEach( item -> {
                //System.out.println( "item=" + item.getClass()   );
                //LinkedHashMap<String, Object> p = (LinkedHashMap<String, Object>) item;
                System.out.println("id:" + item.get("id") );
                System.out.println("firstName:" + item.get("firstName") );
                System.out.println("LastName:" + item.get("lastName") );

            } );
            //System.out.println("response body:" + response.getBody() );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void runPostToRest() {
        Person p0       = new Person();
        p0.setId(1);
        p0.setFirstName("Giovanni");
        p0.setLastName("Verga");
        HttpEntity<Person> request = new HttpEntity<Person>(p0);
        //int respCode = doPostToRest( RESTBASE_URL +"/createPersonWithModel", request);
        int respCode = doPostToRest( RESTBASE_URL +"/createPerson", request);
        //System.out.println("runPost response="+response);
        if( respCode == 200 || respCode == 201) System.out.println("runPost Verga ok" );
        else System.out.println("WARNING: runPost problem:" + respCode);
    }

    protected int doPostToRest(String urlStr, HttpEntity<Person> request)  {
        try{
            RestTemplate rt = new RestTemplate( );

            System.out.println("doPost urlStr=" + urlStr);

            //HttpEntity<Person> request = new HttpEntity<Person>(p0);
            //ResponseEntity<Person> foo = rt.postForEntity(urlStr,request,Person.class);         //OK
            Person foo = rt.postForObject(urlStr, request, Person.class);                         //OK

            //String foo = rt.postForObject(BASE_URL +"/createPerson", request, String.class);
            System.out.println("foo:" + foo  ); //Tutta la pagina
            if( foo != null && foo.getId()==1 ) return 201; else return 0;
                //return foo.getStatusCode().value();
/*
            //ResponseEntity<Person> createResponse  = rt.postForEntity(BASE_URL +"/createPerson", p0, Person.class);
            ResponseEntity<Person> createResponse  = rt.postForEntity(BASE_URL +"/createPersonWithModel", p0, Person.class);
            HttpStatus code = createResponse.getStatusCode();

            System.out.println("response.getStatusCode=" + code.value() );
            //System.out.println("response.getBody      =" + createResponse.getBody() );
            return code.value() ;

 */
        }catch(Exception e){
            System.out.println("response.ERROR =" + e.getMessage() );
            return 0;
        }
    }


    public static void main(String[] args)  {
        RestTemplateRestApiCaller appl = new RestTemplateRestApiCaller();
        appl.runGetLastPerson();
        //appl.runGetAllPersons();
        //appl.getAllPersonsList();
        //appl.runPost();
      }
}
