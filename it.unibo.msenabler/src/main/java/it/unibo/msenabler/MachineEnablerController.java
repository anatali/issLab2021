package it.unibo.msenabler;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MachineEnablerController {
    @PostMapping( "/elaborate" )
    public String elaborate( @RequestParam(name="request") String  msg )  {
         return ("Going to alaborate:" + msg);
    }
}
//curl -d request="todo" localhost:8081/elaborate