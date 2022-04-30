package it.unibo.wsdemoNoSTOMP;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
Optional: utile per eseguire più casi di studio
 */
@Controller
public class WebSocketController {

    @RequestMapping("/basic")
    public String textOnly() {
        return "indexNoImages";
    }

    @RequestMapping("/")
    public String alsoImages() {
        return "indexAlsoImages";
    }

    @RequestMapping("/extra")
    public String getWebSocket() {
        return "myws-broadcast";
    }



}