package it.unibo.msenabler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import it.unibo.enablerCleanArch.main.*;

@Controller
public class HumanEnablerController {

    @Value("${unibo.application.name}")
    String appName; 

    @GetMapping("/")
    public String welcomePage(Model model) {
        System.out.println("HumanEnablerController welcomePage" + model  );
        RadarSystemMainOnPc sys = new RadarSystemMainOnPc();
        RadarSystemConfig.setTheConfiguration(   );
        try{
            sys.setup();
        }catch(Exception e) {
            model.addAttribute("arg", "ERROR " + e.getMessage());
            return "welcome";
        }
        model.addAttribute("arg", appName);
        return "welcome";
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "BaseController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }

}
