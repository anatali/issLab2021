package unibo.SpringDataRest.control;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unibo.SpringDataRest.businessLogic.DataHandler;
import unibo.SpringDataRest.model.Person;


// Annotation
@RestController
@RequestMapping(path = "/RestApi", produces = "application/json")
@CrossOrigin(origins = "*")

public class RestApiController {


    @GetMapping("/getData")
    public Person get() {
        System.out.println(" --- RestApiController get");
        return DataHandler.getLast();
    }


    @PostMapping
    public ResponseEntity<Person> post(@RequestBody Person userData) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(" --- RestApiController post");
        DataHandler.addPerson(userData);
        return new ResponseEntity<>(userData, headers, HttpStatus.CREATED);
    }
}
