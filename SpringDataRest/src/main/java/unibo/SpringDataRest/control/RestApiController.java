package unibo.SpringDataRest.control;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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


    @PostMapping("/createPersonWithModel")
    public ResponseEntity<Person> createPersonWithModel(@RequestBody Person userData) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(" --- RestApiController post");
        DataHandler.addPerson(userData);
        return new ResponseEntity<Person>(userData, headers, HttpStatus.CREATED);
    }

    @PostMapping("/createPerson")
    public String createPerson(@RequestParam( "id" ) String id,
                               @RequestParam( "firstName" ) String firstName,
                               @RequestParam( "lastName" ) String lastName, Model model) {
        Person p = new Person();
        p.setId(Long.valueOf(id));
        p.setFirstName(firstName);
        p.setLastName(lastName);
        System.out.println("............... p="  + p );
        DataHandler.addPerson(p);

        return "";
    }

}
