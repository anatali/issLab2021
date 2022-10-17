package unibo.SpringDataRest.control;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibo.SpringDataRest.businessLogic.DataHandler;
import unibo.SpringDataRest.model.Person;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


@Controller
@RequestMapping("/Api")
public class HIController {
public final String lineSep = "| ";

    private void updateTheModel(Model model, Person lastPerson, String foundPerson){
        model.addAttribute("personmodel", new Person());
        model.addAttribute("lastperson",  lastPerson);
        model.addAttribute("personfound", foundPerson );
        //model.addAttribute("persons", new Vector<Person>() );
    }

    @GetMapping
    public String get(Model model){
        System.out.println("%%% HIController get  "   );
        updateTheModel(model, DataHandler.getLast(), "todo");
        return "PersonGuiNaive";
    }
    @GetMapping("/getAllPersons")
    public String getAllPersons( Model model ){
        System.out.println("%%% HIController getAllPersons  "   );
       /*
            Iterator<Person> iter = DataHandler.getAllPersons( ).iterator();
            String pListOut = "";
            while( iter.hasNext() ){
                pListOut +=  iter.next().toString() ;//lineSep;
            }
            System.out.println( pListOut );*/
        List<Person> lp = DataHandler.getAllPersons( );
        System.out.println( lp );
        updateTheModel(model, DataHandler.getLast(), "todo");
        model.addAttribute("persons", lp );//Further info in page
        return "PersonGuiNaive";
    }
    @GetMapping("/getAPerson") //getAPerson?lastName=Foscolo
    public String getAPerson(Model model, @RequestParam( "lastName" ) String lastName){
        System.out.println("%%% HIController getAPerson lastName= " + lastName );
        String ps = DataHandler.getPersonWithLastName(lastName);
        //System.out.println("getAPerson p=" + p);
        updateTheModel(model, DataHandler.getLast(), ps);
        return "PersonGuiNaive";
    }
    //See https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation
    @PostMapping("/createPersonWithModel")                                            //OK
    //@RequestMapping(value = "/createPerson", method = RequestMethod.POST)  //OK
    public String postWithModel(@ModelAttribute("personmodel") Person newPerson, BindingResult result, Model model) {
        System.out.println("%%% HIController createPersonWithModel "  + result.getTarget() );
        DataHandler.addPerson(newPerson);
        updateTheModel(model, DataHandler.getLast(), "todo");
        return "PersonGuiNaive";
    }
    @PostMapping("/createPerson")                                            //OK
    //@RequestMapping(value = "/createPerson", method = RequestMethod.POST)  //OK
    public String createPerson(@RequestParam( "id" ) String id,
                       @RequestParam( "firstName" ) String firstName,
                       @RequestParam( "lastName" ) String lastName, Model model) {
        System.out.println("%%% HIController createPerson: "  + id + " " + firstName + " " + lastName );
        Person p = new Person();
        p.setId(Long.valueOf(id));
        p.setFirstName(firstName);
        p.setLastName(lastName);
        //System.out.println("............... p="  + p );
        DataHandler.addPerson(p);
        updateTheModel(model, DataHandler.getLast(), "todo");
        return "PersonGuiNaive";
    }

    @DeleteMapping("/deletePerson") //deletePerson?id=ID&firstName=FN&lastName=LN
    public String deletePerson(@RequestParam( "id" ) String id,
                             @RequestParam( "firstName" ) String firstName,
                             @RequestParam( "lastName" ) String lastName,
                             Model model){
        System.out.println("%%% HIController deletePerson: "  + id + " " + firstName + " " + lastName );
        Person p = new Person();
        p.setId(Long.valueOf(id));
        p.setFirstName(firstName);
        p.setLastName(lastName);
        DataHandler.removePerson(p);
        //System.out.println("............... p="  + p );
        updateTheModel(model, p, "REMOVED");
        return "PersonGuiNaive";
    }
}
