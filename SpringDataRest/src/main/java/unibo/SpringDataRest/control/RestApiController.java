package unibo.SpringDataRest.control;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import unibo.SpringDataRest.businessLogic.DataHandler;
import unibo.SpringDataRest.model.Person;

import java.util.List;


// Annotation
@RestController
@RequestMapping(path = "/RestApi", produces = "application/json")
@CrossOrigin(origins = "*")

public class RestApiController {

    @GetMapping("/getLastPerson")
    public Person getLastPerson() {
        System.out.println(" --- RestApiController getLastPerson");
        return DataHandler.getLast();  //Restituice un oggetto Java di class Person
        //poichè produce "application/json" i dati sono convertiti in Json
        //Ad esempio:{"id":2,"firstName":"Alessando","lastName":"Manzoni"}
    }
    @GetMapping("/getAllPersons")
    public List<Person> getAllPersons() {
        System.out.println(" --- RestApiController getAllPersons");
        return DataHandler.getAllPersons();
    }

    @PostMapping("/createPersonWithModel")
    public ResponseEntity<Person> createPersonWithModel(@RequestBody Person userData) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(" --- RestApiController post");
        DataHandler.addPerson(userData);
        return new ResponseEntity<Person>(userData, headers, HttpStatus.CREATED);
    }

    @PostMapping("/createPerson")
    public String createPersonWithParams(@RequestParam( "id" ) String id,
                               @RequestParam( "firstName" ) String firstName,
                               @RequestParam( "lastName" ) String lastName, Model model) {
        Person p = new Person();
        p.setId(Long.valueOf(id));
        p.setFirstName(firstName);
        p.setLastName(lastName);
        //System.out.println("............... p="  + p );
        DataHandler.addPerson(p);
        return "";
    }
    @DeleteMapping("/deletePerson") //deletePerson?id=ID&firstName=FN&lastName=LN
    public String deletePerson(@RequestParam( "id" ) String id,
                               @RequestParam( "firstName" ) String firstName,
                               @RequestParam( "lastName" ) String lastName,
                               Model model) {
        System.out.println("%%% HIController deletePerson: " + id + " " + firstName + " " + lastName);
        Person p = new Person();
        p.setId(Long.valueOf(id));
        p.setFirstName(firstName);
        p.setLastName(lastName);
        DataHandler.removePerson(p);
        return "";
    }
}
