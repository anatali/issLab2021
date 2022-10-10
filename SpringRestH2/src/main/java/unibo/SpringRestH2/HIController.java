package unibo.SpringRestH2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HIController {

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<String> getProducts() {
        List<String> productsList = new ArrayList<>();
        productsList.add("Honey");
        productsList.add("Almond");
        return productsList;
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "HIController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }
}