package unibo.SpringDataRest.control;
//https://www.geeksforgeeks.org/spring-resttemplate/

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibo.SpringDataRest.businessLogic.DataHandler;
import unibo.SpringDataRest.model.Person;


@Controller
@RequestMapping("/Api")
public class HIController {

    private void updateTheModel(Model model, Person lastPerson, String foundPerson){
        model.addAttribute("personmodel", new Person());
        model.addAttribute("lastperson",  lastPerson);
        model.addAttribute("personfound", foundPerson );
    }

    @GetMapping
    public String get(Model model){
        updateTheModel(model, DataHandler.getLast(), "todo");
        return "PersonGuiNaive";
    }
    @GetMapping("/getAPerson") //getAPerson?lastName=Foscolo
    public String getAPerson(Model model, @RequestParam( "lastName" ) String lastName){
        String ps = DataHandler.getPersonWithLastName(lastName);
        //System.out.println("getAPerson p=" + p);
        updateTheModel(model, DataHandler.getLast(), ps);
        return "PersonGuiNaive";
    }
    //See https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation
    @PostMapping("/createPersonWithModel")                                            //OK
    //@RequestMapping(value = "/createPerson", method = RequestMethod.POST)  //OK
    public String postWithModel(@ModelAttribute("personmodel") Person newPerson, BindingResult result, Model model) {
        System.out.println("mmmmmmmmmmmmmmmmmmmmmmm "  + result.getTarget() );
        DataHandler.addPerson(newPerson);
        updateTheModel(model, DataHandler.getLast(), "todo");
        return "PersonGuiNaive";
    }
    @PostMapping("/createPerson")                                            //OK
    //@RequestMapping(value = "/createPerson", method = RequestMethod.POST)  //OK
    public String post(@RequestParam( "id" ) String id,
                       @RequestParam( "firstName" ) String firstName,
                       @RequestParam( "lastName" ) String lastName, Model model) {
        System.out.println("..............."  + id + " " + firstName + " " + lastName );
        //DataHandler.addPerson(newPerson);
        updateTheModel(model, DataHandler.getLast(), "todo");
        return "PersonGuiNaive";
    }

}
