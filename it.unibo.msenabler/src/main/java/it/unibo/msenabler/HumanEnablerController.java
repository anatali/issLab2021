package it.unibo.msenabler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
//import it.unibo.enablerCleanArch.main.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;

@Controller
public class HumanEnablerController {
    public static RadarSystemDevicesOnRaspMqtt sysClient ;

    @Value("${unibo.application.name}")
    String appName;

    @GetMapping("/")
    public String welcomePage(Model model) {
        if( sysClient == null ){ sysClient = MsenablerApplication.sys; }
        model.addAttribute("ledstate", "false (perhaps)");
        model.addAttribute("arg", appName);
        model.addAttribute("ledgui","ledOff");
        System.out.println("HumanEnablerController welcomePage" + model + " sysClient=" + sysClient );
        return "gui";
    }

    //protected void activateTheDevices(){
    //    new RadarSystemDevicesOnRaspMqtt().doJob("RadarSystemConfig.json");
    //}
//    protected void activateTheClient(){
//        sysClient = new RadarSystemMainOnPcMqtt();
//    }

    @PostMapping( path = "/on" )
    public String doOn( @RequestParam(name="cmd", required=false, defaultValue="")
                    String moveName, Model model){
        //System.out.println("HumanEnablerController doOn sys=" + sys  );
        //activateTheClient();
        //if( sysClient == null ){ sysClient = MsenablerApplication.sys; }
        if( sysClient != null ){
            sysClient.ledActivate(true);
            String ledState = sysClient.ledState();
            System.out.println("HumanEnablerController doOn ledState=" + ledState  );
            model.addAttribute("ledgui","ledOn");
            model.addAttribute("ledstate", ledState);
        }
        model.addAttribute("arg", appName+" After Led on");
        //model.addAttribute("ledstate", "ledOn");
        return "gui";
    }



    @PostMapping( path = "/off" )
    public String doOff(@RequestParam(name="cmd", required=false, defaultValue="")
                        String moveName, Model model){
        //activateTheClient();
        //if( sysClient == null ){ sysClient = MsenablerApplication.sys; }
        if( sysClient != null ){
            sysClient.ledActivate(false);
            String ledState = sysClient.ledState();
            System.out.println("HumanEnablerController doOff ledState=" + ledState  );
            model.addAttribute("ledgui","ledOff");
            model.addAttribute("ledstate", ledState);
        }
         model.addAttribute("arg", appName+" After Led off");
        return "gui";
    }

    @PostMapping( path = "/doLedBlink" )
    public String doBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                String moveName, Model model){
        if( sysClient != null ) sysClient.doLedBlink( );
        model.addAttribute("arg", appName+" After Led blink");
        model.addAttribute("ledstate","ledBlinking ...");
        return "gui";
    }

    @PostMapping( path = "/stopLedBlink" )
    public String stopLedBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        if( sysClient != null ) sysClient.stopLedBlink( );
        String ledState = sysClient.ledState();
        System.out.println("HumanEnablerController stopLedBlink ledState=" + ledState  );
        model.addAttribute("arg", appName+" After Led stop blink");
        if(ledState.equals("true"))  model.addAttribute("ledgui","ledOn");
        else model.addAttribute("ledgui","ledOff");
        model.addAttribute("ledstate",ledState);
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
