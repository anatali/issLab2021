package unibo.SpringDataRest.control;
//https://www.geeksforgeeks.org/spring-resttemplate/

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import unibo.SpringDataRest.model.UserData;


@Controller
@RequestMapping("/Api")
public class UserDataController {

    @GetMapping
    public String get(Model model){
        RestTemplateProvider restTemplate = new RestTemplateProvider();
        model.addAttribute("user", restTemplate.getUserData());
        model.addAttribute("model", new UserData());
        return "GetData";
    }

    @PostMapping
    public String post(@ModelAttribute("model") UserData user, Model model) {
        RestTemplateProvider restTemplate = new RestTemplateProvider();
        ResponseEntity<UserData> response = restTemplate.post(user);
        model.addAttribute("user", response.getBody());
        model.addAttribute("headers",
                response.getHeaders() + " "
                        + response.getStatusCode());
        return "GetData";
    }
}
