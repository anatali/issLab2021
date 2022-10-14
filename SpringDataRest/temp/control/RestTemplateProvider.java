package unibo.SpringDataRest.control;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import unibo.SpringDataRest.model.UserData;

public class RestTemplateProvider {
    // Creating an instance of RestTemplate class
    RestTemplate rest = new RestTemplate();

    // Method
    public UserData getUserData() {
        return rest.getForObject(
                "http://localhost:8080/RestApi/getData",
                UserData.class);
    }

    // Method
    public ResponseEntity<UserData> post(UserData user) {
        return rest.postForEntity(
                "http://localhost:8080/RestApi", user,
                UserData.class, "");
    }

}




