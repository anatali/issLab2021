package unibo.SpringDataRest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import unibo.SpringDataRest.model.Person;

import java.util.List;

@RestController
public class Controller {
/*
Si veda
    https://www.digitalocean.com/community/tutorials/spring-resttemplate-example
    https://www.digitalocean.com/community/tutorials/spring-data-jpa
    Codice in SpringRestTemplate

 */
    @Autowired
    PersonService personService;

    @RequestMapping(value = "/person/{name}", method = RequestMethod.GET)
    public @ResponseBody Person getAllUsers(@PathVariable String name) {
        List<Person> pl = personService.getByName(name);
        return pl.get(0);
    }
}
