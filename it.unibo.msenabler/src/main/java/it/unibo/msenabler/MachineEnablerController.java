package it.unibo.msenabler;

import it.unibo.enablerCleanArch.supports.Colors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//See https://www.baeldung.com/rest-template
@RestController
public class MachineEnablerController {
    @PostMapping( "/photo" )
    public String elaborate( @RequestParam(name="request") String  msg ){
        Colors.out("MachineEnablerController | elaborate " + msg.length() );
         return ("Going to manage photo:" + msg.length());
    }
}
//curl -d request="todo" localhost:8081/photo