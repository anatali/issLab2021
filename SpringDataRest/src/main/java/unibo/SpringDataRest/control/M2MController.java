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

public class M2MController {

    @GetMapping("/getLastPerson")
    public ResponseEntity<Person> getLastPerson() {
        System.out.println(" --- M2MController getLastPerson");
        Person p = DataHandler.getLast();  //Restituice un oggetto Java di class Person
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<Person>(p, headers, HttpStatus.CREATED);
    }
    @GetMapping("/getAPerson")
    public ResponseEntity<Person> getAPerson(@RequestParam( "lastName" ) String lastName) {
        System.out.println(" --- M2MController getAPerson");
        Person p = DataHandler.getThePersonWithLastName(lastName);   
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<Person>(p, headers, HttpStatus.OK);
    }
    @GetMapping("/getAllPersons")
    public List<Person> getAllPersons() {
        System.out.println(" --- M2MController getAllPersons");
        return DataHandler.getAllPersons();
    }

    @PostMapping("/createPersonWithModel")
    public ResponseEntity<Person> createPersonWithModel(@RequestBody Person userData) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(" --- M2MController createPersonWithModel");
        DataHandler.addPerson(userData);
        return new ResponseEntity<Person>(userData, headers, HttpStatus.CREATED);
    }

    @PostMapping("/createPerson")
    public String createPersonWithParams(@RequestParam( "id" ) String id,
                               @RequestParam( "firstName" ) String firstName,
                               @RequestParam( "lastName" ) String lastName ) {
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
        System.out.println("%%% M2MController deletePerson: " + id + " " + firstName + " " + lastName);
        Person p = new Person();
        p.setId(Long.valueOf(id));
        p.setFirstName(firstName);
        p.setLastName(lastName);
        DataHandler.removePerson(p);
        return "";
    }
}
