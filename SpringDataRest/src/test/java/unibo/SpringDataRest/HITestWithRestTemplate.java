package unibo.SpringDataRest;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import unibo.SpringDataRest.callers.PageUtil;
import unibo.SpringDataRest.callers.RestTemplateApiUtil;
import unibo.SpringDataRest.model.Person;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


//See https://www.baeldung.com/rest-template
//@SpringBootTest
public class HITestWithRestTemplate {
    //private static RestTemplate rt ;
    private static RestTemplateApiUtil rtUtil;


    private void ckeckPerson( String lastName, String expected){
        ResponseEntity<String> response =  rtUtil.getAPerson(lastName);
        String answer = PageUtil.readTheHtmlPage(response.getBody(), "FOUND");
        //System.out.println("ckeckPerson answer:" + answer );
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        assertTrue( answer.contains(expected));
    }

    @BeforeAll
    public static void start() throws Exception {
        SpringDataRestApplication.main( new String[]{});
        rtUtil = new RestTemplateApiUtil("http://localhost:8080/Api");
    }

    @AfterAll
    public static void end(){
        SpringDataRestApplication.closeAppl();
    }


    @Test
    public void testGetFoscoloInitial(){
        System.out.println("=== testGetFoscoloInitial"  );
        ckeckPerson("Foscolo","person not found" );
    }

    @Test
    public void testGetFoscoloAfterCreate(){
        System.out.println("=== testGetFoscoloAfterCreate"  );
        ckeckPerson("Foscolo","person not found" );
        //CREATE
        ResponseEntity<String> response =
                rtUtil.createPerson("1","Ugo","Foscolo");
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        //CHECK
        ckeckPerson("Foscolo","lastName=Foscolo" );
        //DELETE
        response = rtUtil.deletePerson("1","Ugo","Foscolo");
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        //CHECK
        ckeckPerson("Foscolo","person not found" );
    }

    //@Test
    public void shouldReturnRepositoryIndex() {
        System.out.println(" ------ shouldReturnRepositoryIndex"  );
        String url      =  "http://localhost:8080/";
        //RestTemplate rt = new RestTemplate( );
        //ResponseEntity<String> response = rtUtil.getForEntity( url, String.class);
        //System.out.println("response:" + response );
        //Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }




}
