package it.unibo.msenabler;

import it.unibo.enablerCleanArch.supports.Colors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MachineEnablerController {
    @PostMapping( "/photo" )
    public String elaborate( @RequestParam(name="request") String  msg )  {
        Colors.out("MachineEnablerController | elaborate " + msg);
         return ("Going to manage photo:" + msg);
    }
}
//curl -d request="todo" localhost:8081/photo