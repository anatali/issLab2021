package it.unibo.wsdemoNoSTOMP;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
Optional: utile per eseguire più casi di studio
 */
@Controller
public class WebSocketController {

    @RequestMapping("/")
    public String textOnly() {
        return "indexNoImages";
    }

    @RequestMapping("/alsoimages")
    public String alsoImages() {
        return "indexAlsoImages";
    }

    @RequestMapping("/extra")
    public String getWebSocket() {
        return "ws-broadcast";
    }


}