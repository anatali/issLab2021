package unibo.SpringDataRest.control;
//https://www.geeksforgeeks.org/spring-resttemplate/

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import unibo.SpringDataRest.businessLogic.DataHandler;
import unibo.SpringDataRest.model.Person;


//@Controller
@RequestMapping("/Api")
public class HIControllerUsingRest {

    @GetMapping
    public String get(Model model){
        //RestTemplateProvider restTemplate = new RestTemplateProvider();
        //model.addAttribute("user", restTemplate.getUserData());
        updateTheModel(model, DataHandler.getLast(), "todo");
        //model.addAttribute("model", new Person());
        //model.addAttribute("lastperson",  DataHandler.getLast());
        //model.addAttribute("personfound", DataHandler.getFirst());
        //model.addAttribute("headers", response.getHeaders() + " " + response.getStatusCode());
        return "GetData";
    }
    @GetMapping("/getAPerson") //getAPerson?lastName=Foscolo
    public String getAPerson(Model model, @RequestParam( "lastName" ) String lastName){
        String ps = DataHandler.getPersonWithLastName(lastName);
        //System.out.println("getAPerson p=" + p);
        updateTheModel(model, DataHandler.getLast(), ps);
        //model.addAttribute("model",      new Person());
        //model.addAttribute("lastperson", DataHandler.getLast());
        //model.addAttribute("personfound", ps );
         return "GetData";
    }
    @PostMapping
    public String post(@ModelAttribute("model") Person userData, Model model) {
        /*
        RestTemplateProvider restTemplate = new RestTemplateProvider();
        ResponseEntity<Person> response = restTemplate.post(user);
        model.addAttribute("person", response.getBody());
        model.addAttribute("headers",
                response.getHeaders() + " " + response.getStatusCode());

         */

        DataHandler.addPerson(userData);
        updateTheModel(model, DataHandler.getLast(), "todo");
        //model.addAttribute("lastperson",  DataHandler.getLast());
        //model.addAttribute("personfound", DataHandler.getFirst());
        return "GetData";
    }

    private void updateTheModel(Model model, Person lastPerson, String foundPerson){
        model.addAttribute("model",      new Person());
        model.addAttribute("lastperson", lastPerson);
        model.addAttribute("personfound", foundPerson );
    }
}
