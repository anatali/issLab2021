package unibo.SpringDataRest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.Person;

/*
The @SpringBootTest annotation tells Spring Boot to look for a main configuration class
(one with @SpringBootApplication, for instance) and use that to start a Spring application context.
 */
@SpringBootTest
public class FirstTest {

    /*
    the application context is cached between tests
     */
    //@Test
    public void contextLoads() throws Exception {
        System.out.println("%%% contextLoads");
        //assertThat(controller).isNotNull();
    }
    @Test
    public void test0(){
        //String getPeopleUrl = "http://localhost:8080/people"; //+ "/1"
        String getPeopleUrl = "http://localhost:8080/person/Baggins";

        //https://www.baeldung.com/rest-template
        //HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        //RestTemplate rt = new RestTemplate(factory);

        RestTemplate rt = new RestTemplate( );
        System.out.println("%%% test0 rt="+rt + " getPeopleUrl=" + getPeopleUrl);
        /*
        Person p0 = new Person();
        p0.setFirstName("Giacomo");
        p0.setLastName("Leopardi");
        HttpEntity<Person> request = new HttpEntity<Person>(p0);
        Person foo = rt.postForObject(getPeopleUrl, request, Person.class);
        System.out.println("foo=" + foo );
*/
        ResponseEntity<String> response = rt.getForEntity( getPeopleUrl, String.class);
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
