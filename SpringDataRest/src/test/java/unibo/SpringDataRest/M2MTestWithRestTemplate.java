package unibo.SpringDataRest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import unibo.SpringDataRest.callers.PageUtil;
import unibo.SpringDataRest.callers.RestTemplateApiUtil;

import static org.junit.jupiter.api.Assertions.assertTrue;


//See https://www.baeldung.com/rest-template
//@SpringBootTest
public class M2MTestWithRestTemplate {
    private static RestTemplateApiUtil rtUtil;


    private void ckeckPersonRestApi( String lastName, boolean expected){
        ResponseEntity<String> response =  rtUtil.getAPerson(lastName);
        //System.out.println("ckeckPersonRestApi answer:" + response );
        System.out.println(" ... ckeckPersonRestApi answer:" + response.getBody() );
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        if( expected ) assertTrue( response.getBody() != null );
        else assertTrue( response.getBody() == null );
    }

    @BeforeAll
    public static void start() throws Exception {
        SpringDataRestApplication.main( new String[]{});
        rtUtil = new RestTemplateApiUtil("http://localhost:8080/RestApi");
    }

    @AfterAll
    public static void end(){
        SpringDataRestApplication.closeAppl();
    }


    @Test
    public void testGetFoscoloInitial(){
        System.out.println("=== testGetFoscoloInitial"  );
        ckeckPersonRestApi("Foscolo", false  );
    }

    @Test
    public void testGetFoscoloAfterCreate(){
        System.out.println("=== testGetFoscoloAfterCreate"  );
        ckeckPersonRestApi("Foscolo", false );
        //CREATE
        ResponseEntity<String> response =
                rtUtil.createPerson("1","Ugo","Foscolo");
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        //CHECK
        ckeckPersonRestApi("Foscolo", true  );
        //DELETE
        response = rtUtil.deletePerson("1","Ugo","Foscolo");
        assertTrue(response.getStatusCode()==HttpStatus.OK);
        //CHECK
        ckeckPersonRestApi("Foscolo", false );
    }






}
