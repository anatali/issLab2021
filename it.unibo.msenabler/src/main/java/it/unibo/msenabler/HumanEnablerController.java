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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HumanEnablerController {
private RadarSystemMainOnPcMqtt sysClient;

    @Value("${unibo.application.name}")
    String appName; 

    @GetMapping("/")
    public String welcomePage(Model model) {
        System.out.println("HumanEnablerController welcomePage" + model  );
         //RadarSystemDevicesOnRaspMqtt sys = new RadarSystemDevicesOnRaspMqtt();
         //RadarSystemConfig.setTheConfiguration(   );
         try{
             //new RadarSystemDevicesOnRaspMqtt().doJob("RadarSystemConfig.json");
             sysClient = new RadarSystemMainOnPcMqtt();
             //sys.doJob("RadarSystemConfig.json");
        }catch(Exception e) {
            model.addAttribute("arg", "ERROR " + e.getMessage());
            return "welcome";
        }
        model.addAttribute("arg", appName);
        //return "welcome";

        return "gui";
    }

    @PostMapping( path = "/on" )
    public String doOn( @RequestParam(name="cmd", required=false, defaultValue="")
                    String moveName, Model model){
        //System.out.println("HumanEnablerController doOn sys=" + sys  );
        if( sysClient != null ) sysClient.ledActivate(true);
        return "gui";
    }

    @PostMapping( path = "/off" )
    public String doOff(@RequestParam(name="cmd", required=false, defaultValue="")
                        String moveName, Model model){
        if( sysClient != null ) sysClient.ledActivate(false);
        return "gui";
    }

    @PostMapping( path = "/doLedBlink" )
    public String doBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                String moveName, Model model){
        if( sysClient != null ) sysClient.doLedBlink( );
        return "gui";
    }

    @PostMapping( path = "/stopLedBlink" )
    public String stopLedBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        if( sysClient != null ) sysClient.stopLedBlink( );
        return "gui";
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "BaseController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }

}
