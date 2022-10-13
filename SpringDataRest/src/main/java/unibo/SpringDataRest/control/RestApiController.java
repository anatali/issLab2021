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
import unibo.SpringDataRest.model.UserData;


// Annotation
@RestController
@RequestMapping(path = "/RestApi", produces = "application/json")
@CrossOrigin(origins = "*")

public class RestApiController {

    @GetMapping("/getData")
    public UserData get() {
        UserData userData = new UserData();
        userData.setId("1");
        userData.setUserName("an@unibo.it");
        userData.setData("Data send by Rest-API");
        System.out.println(" --- RestApiController get");
        return userData;
    }


    @PostMapping
    public ResponseEntity<UserData> post(@RequestBody UserData userData) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(" --- RestApiController post");
        return new ResponseEntity<>(userData, headers, HttpStatus.CREATED);
    }
}
