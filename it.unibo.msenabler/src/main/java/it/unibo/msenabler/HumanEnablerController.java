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
    public static RadarSystemMainOnPcMqtt sysClient;

    @Value("${unibo.application.name}")
    String appName;

    @GetMapping("/")
    public String welcomePage(Model model) {
        model.addAttribute("ledarg", "false (perhaps)");
        model.addAttribute("arg", appName);
        model.addAttribute("ledgui","ledOff");
        System.out.println("HumanEnablerController welcomePage" + model  );
        return "gui";
    }

    //protected void activateTheDevices(){
    //    new RadarSystemDevicesOnRaspMqtt().doJob("RadarSystemConfig.json");
    //}
    protected void activateTheClient(){
        sysClient = new RadarSystemMainOnPcMqtt();
    }

    @PostMapping( path = "/on" )
    public String doOn( @RequestParam(name="cmd", required=false, defaultValue="")
                    String moveName, Model model){
        //System.out.println("HumanEnablerController doOn sys=" + sys  );
        activateTheClient();
        if( sysClient != null ){
            sysClient.ledActivate(true);
            String ledState = sysClient.ledState();
            System.out.println("HumanEnablerController doOn ledState=" + ledState  );
            model.addAttribute("ledgui","ledOn");
            model.addAttribute("ledarg", ledState);
        }
        model.addAttribute("arg", appName+" After Led on");
        //model.addAttribute("ledarg", "ledOn");
        return "gui";
    }



    @PostMapping( path = "/off" )
    public String doOff(@RequestParam(name="cmd", required=false, defaultValue="")
                        String moveName, Model model){
        activateTheClient();
        if( sysClient != null ){
            sysClient.ledActivate(false);
            String ledState = sysClient.ledState();
            System.out.println("HumanEnablerController doOff ledState=" + ledState  );
            model.addAttribute("ledgui","ledOff");
            model.addAttribute("ledarg", ledState);
        }
         model.addAttribute("arg", appName+" After Led off");
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
