package unibo.SpringDataRest.control;
//https://www.geeksforgeeks.org/spring-resttemplate/
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.Person;

public class RestTemplateProvider {
    // Creating an instance of RestTemplate class
    RestTemplate rest = new RestTemplate();

    // Method
    public Person getUserData() {
        return rest.getForObject(
                "http://localhost:8080/RestApi/getData", Person.class);
    }

    // Method
    public ResponseEntity<Person> post(Person user) {
        return rest.postForEntity(
                "http://localhost:8080/RestApi", user,
                Person.class, "");
    }

}




