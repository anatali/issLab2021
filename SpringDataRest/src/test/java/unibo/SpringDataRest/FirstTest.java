package unibo.SpringDataRest;

import org.junit.jupiter.api.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.Person;


//See https://www.baeldung.com/rest-template
public class FirstTest {



    private HttpHeaders prepareBasicAuthHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        //final String encodedLogPass = getBase64EncodedLogPass();
        //headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encodedLogPass);
        return headers;
    }

    @BeforeEach
    public void start() throws Exception {
        SpringDataRestApplication.main( new String[]{});
    }

    @AfterAll
    public static void end(){
        SpringDataRestApplication.closeAppl();
    }

    //@Test
    public void shouldReturnRepositoryIndex() {
        System.out.println(" ------ shouldReturnRepositoryIndex"  );
        String url      =  "http://localhost:8080/";
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt.getForEntity( url, String.class);
        System.out.println("response:" + response );
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
    //@Test
    public void getPeople() {
        System.out.println(" ------ getPeople"  );
        String url = "http://localhost:8080/people";
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt.getForEntity( url, String.class);
        System.out.println("response:" + response );
        System.out.println("response body:" + response.getBody() );
    }
    @Test
    public void createAPerson() {
        String url = "http://localhost:8080/people";
        RestTemplate rt = new RestTemplate( );

        System.out.println(" ------ createPeople with POST"  );
        Person p0       = new Person();
        p0.setFirstName("Giovanni");
        p0.setLastName("Verga");
        System.out.println("p0:" + p0  );
        //HttpEntity<Person> request = new HttpEntity<Person>(p0);
        ResponseEntity<Person> createResponse  = rt.postForEntity(url, p0, Person.class);
        System.out.println("response.getStatusCode=" + createResponse.getStatusCode() );
        System.out.println("response.getBody      =" + createResponse.getBody() );
/*
        System.out.println(" ------ createPeople again with POST"  );
        //Person p1      = new Person();
        p1.setFirstName("Ugo");
        p1.setLastName("Foscolo");
        System.out.println("p1:" + p1  );
        request          = new HttpEntity<Person>(p1);
        createResponse   = rt.postForObject(url, request, Person.class);
        System.out.println("response=" + createResponse );
*/


        System.out.println(" ------ getAPerson"  );
        String queryurl = "http://localhost:8080/people/1";
        ResponseEntity<String> answer = rt.getForEntity( queryurl, String.class);
        System.out.println("answer                :" + answer );
        System.out.println("answer.getStatusCode():" + answer.getStatusCode() );
        System.out.println("answer.getBody()      :" + answer.getBody() );

        System.out.println(" ------ getAPerson again"  );
        queryurl = "http://localhost:8080/people";
        Person response = rt.getForObject( queryurl, Person.class);
        System.out.println("response:" + response );


        /*
        System.out.println(" ------ getAPerson again"  );
        queryurl = "http://localhost:8080/people/1";
        ResponseEntity<Person> answerPerson = rt.getForEntity( queryurl, Person.class);
        //ResponseEntity<String> answerPerson = rt.getForEntity( queryurl, String.class);
        System.out.println("answerPerson:" + answerPerson.getBody() );

        Person updatePerson = new Person();
        updatePerson.setId( createResponse.getId() );
        updatePerson.setFirstName("GIOVANNI");
        updatePerson.setLastName("VERGA");

        HttpHeaders headers = prepareBasicAuthHeaders();
        HttpEntity<Person> request1 = new HttpEntity<>(updatePerson, headers);
        ResponseEntity<Person> createResponse1 = rt.exchange(queryurl, HttpMethod.POST, request, Person.class);
        updatedInstance.setId(createResponse1.getBody().getId());

 */
    }
    //@Test
    public void getAPerson(){
        System.out.println(" ------ getAPerson"  );
        String url = "http://localhost:8080/person/Baggins";
        RestTemplate rt = new RestTemplate( );
        System.out.println("%%% test0 rt="+rt + " url=" + url);
        /*

*/
        ResponseEntity<String> response = rt.getForEntity( url, String.class);
        //Person p = rt.getForObject("http://localhost:8080/people/search/findByLastName?name=Leopardi", Person.class);
        //System.out.println("" + response);
        //org.springframework.data.rest.webmvc.RepositoryController c;
        //org.springframework.data.rest.webmvc.RepositoryEntityController  //not public
        System.out.println("response:" + response );
        //We can also map the response directly to a Resource DTO
    }

/*
    @Test
    public void test1() {
       WebClient client = WebClient.create("http://localhost:8080");

        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();

    }*/
}
