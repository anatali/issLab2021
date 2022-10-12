package unibo.SpringDataRest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.Person;

@SpringBootTest
public class FirstTest {

    @Test
    public void test0(){
        RestTemplate rt = new RestTemplate();
        Person p = rt.getForObject("http://localhost:8080/people/search/findByLastName?name=Leopardi", Person.class);
        System.out.println("" + p);
    }
}
