package it.unibo.msenabler;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;


@Controller
public class HumanEnablerController {
	private boolean basicGui    = false;
	private boolean sonarDataOn = false;
    public static RadarSystemDevicesOnRaspMqtt applMqtt;

    @Value("${unibo.application.name}")
    String appName;

    //@GetMapping("/")
    @RequestMapping("/")
    public String welcomePage(Model model) {
    	
        if( applMqtt == null ){ applMqtt = MsenablerApplication.sys; }
        model.addAttribute("ledstate", "false (perhaps)");
        model.addAttribute("arg", appName);
        model.addAttribute("ledgui","ledOff");
        Colors.out("HumanEnablerController welcomePage" + model + " sysClient=" + applMqtt);
        Colors.out("HumanEnablerController sonar active=" + applMqtt.sonarIsactive()  );
        
        basicGui = false;
        return "RadarSystemUserConsole";
    }
    @RequestMapping("/basic")
    public String basicPage(Model model) {
        if( applMqtt == null ){ applMqtt = MsenablerApplication.sys; }
        model.addAttribute("ledstate", "false (perhaps)");
        model.addAttribute("arg", appName);
        model.addAttribute("ledgui","ledOff");
        Colors.out("HumanEnablerController welcomePage" + model + " sysClient=" + applMqtt);
        Colors.out("HumanEnablerController sonar active=" + applMqtt.sonarIsactive()  );
        basicGui = true;
        return "RadarSystemUserGui";  
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
    	Colors.out("HumanEnablerController doOn "   );
        //activateTheClient();
        //if( sysClient == null ){ sysClient = MsenablerApplication.sys; }
        if( applMqtt != null ){
            applMqtt.ledActivate(true);
            String ledState = applMqtt.ledState();
            Colors.out("HumanEnablerController doOn ledState=" + ledState  );
            model.addAttribute("ledgui","ledOn");
            model.addAttribute("ledstate", ledState);
        }
        model.addAttribute("arg", appName+" After Led on");
        //model.addAttribute("ledstate", "ledOn");
        if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }



    @PostMapping( path = "/off" )
    public String doOff(@RequestParam(name="cmd", required=false, defaultValue="")
                        String moveName, Model model){
        //activateTheClient();
        //if( sysClient == null ){ sysClient = MsenablerApplication.sys; }
        if( applMqtt != null ){
            applMqtt.ledActivate(false);
            String ledState = applMqtt.ledState();
            Colors.out("HumanEnablerController doOff ledState=" + ledState  );
            model.addAttribute("ledgui","ledOff");
            model.addAttribute("ledstate", ledState);
        }
         model.addAttribute("arg", appName+" After Led off");
         if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/doLedBlink" )
    public String doBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                String moveName, Model model){
        if( applMqtt != null ) applMqtt.doLedBlink( );
        model.addAttribute("arg", appName+" After Led blink");
        model.addAttribute("ledstate","ledBlinking ...");
        if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/stopLedBlink" )
    public String stopLedBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        if( applMqtt != null ) applMqtt.stopLedBlink( );
        String ledState = applMqtt.ledState();
        Colors.out("HumanEnablerController stopLedBlink ledState=" + ledState  );
        model.addAttribute("arg", appName+" After Led stop blink");
        if(ledState.equals("true"))  model.addAttribute("ledgui","ledOn");
        else model.addAttribute("ledgui","ledOff");
        model.addAttribute("ledstate",ledState);
        if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/distance" )
    public String distance(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        String d = "unknown";
        Colors.out("HumanEnablerController DISTANCE - sonar active=" + applMqtt.sonarIsactive()  );
        if( applMqtt != null ){
            if( ! applMqtt.sonarIsactive() ) applMqtt.sonarActivate();
            d = applMqtt.sonarDistance();
            Colors.out("HumanEnablerController sonar d=" + d + " sonarDelay=" + RadarSystemConfig.sonarDelay );
        }            	
        model.addAttribute("arg", appName+" After distance");
        model.addAttribute("sonardistance",d);
        if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/sonardataon" )
    public String sonardataon(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
    	if( sonarDataOn ) {
    		if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    	}
        Colors.out("HumanEnablerController sonardataon - sonar active=" + applMqtt.sonarIsactive()  );
        if( applMqtt != null ){
            if( ! applMqtt.sonarIsactive() ) applMqtt.sonarActivate();
            sonarDataOn = true;
            WebSocketHandler h = WebSocketHandler.getWebSocketHandler();
            new Thread() {
            	public void run() {
                    while( applMqtt.sonarIsactive() && sonarDataOn ) {
                    	String d = applMqtt.sonarDistance();
                    	Colors.out("HumanEnablerController sonar d=" + d + " sonarDelay=" + RadarSystemConfig.sonarDelay );
                        Utils.delay(RadarSystemConfig.sonarDelay);
                        //update on ws
                        try {
							h.sendToAll( d );
						} catch (Exception e) {
 							Colors.outerr("ws update ERROR:" + e.getMessage());
 							sonarDataOn = false;
 							applMqtt.sonarDectivate();
						}
                    }   
                    sonarDataOn = false;
            	}
            }.start();
        }
//        model.addAttribute("arg", appName+" After distance");
//        model.addAttribute("sonardistance",d);
          //if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
        if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    
    @PostMapping( path = "/sonardataoff" )
    public String sonardataoff(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
    	sonarDataOn = false;
        if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    
    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "BaseController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }

}
