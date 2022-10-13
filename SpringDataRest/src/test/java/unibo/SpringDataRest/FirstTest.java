package unibo.SpringDataRest;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


//See https://www.baeldung.com/rest-template
public class FirstTest {


    @BeforeEach
    public void start() throws Exception {
        SpringDataRestApplication.main( new String[]{});
    }

    @AfterAll
    public static void end(){
        SpringDataRestApplication.closeAppl();
    }

    @Test
    public void shouldReturnRepositoryIndex() {
        System.out.println(" ------ shouldReturnRepositoryIndex"  );
        String url      =  "http://localhost:8080/";
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt.getForEntity( url, String.class);
        System.out.println("response:" + response );
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
    @Test
    public void getPeople() {
        System.out.println(" ------ getPeople"  );
        String url = "http://localhost:8080/people";
        RestTemplate rt = new RestTemplate( );
        ResponseEntity<String> response = rt.getForEntity( url, String.class);
        System.out.println("response:" + response );
        System.out.println("response body:" + response.getBody() );

    }
    //@Test
    public void getAPerson(){
        System.out.println(" ------ getAPerson"  );
        String url = "http://localhost:8080/person/Baggins";
        RestTemplate rt = new RestTemplate( );
        System.out.println("%%% test0 rt="+rt + " url=" + url);
        /*
        Person p0 = new Person();
        p0.setFirstName("Giacomo");
        p0.setLastName("Leopardi");
        HttpEntity<Person> request = new HttpEntity<Person>(p0);
        Person foo = rt.postForObject(getPeopleUrl, request, Person.class);
        System.out.println("foo=" + foo );
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
