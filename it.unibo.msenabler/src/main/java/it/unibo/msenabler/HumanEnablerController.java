package it.unibo.msenabler;


import it.unibo.enablerCleanArch.domain.IApplicationFacade;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private boolean allOnRasp   = MsenablerApplication.allOnRasp;
	private boolean sonarDataOn = false;
    public static IApplicationFacade appl; //RadarSystemDevicesOnRaspMqtt appl;

    @Value("${unibo.application.name}")
    String appName;

    @GetMapping("/")
    //@RequestMapping("/")
    public String welcomePage(Model model) {    	
        if( appl == null ){ appl = allOnRasp ? MsenablerApplication.sysMqtt : MsenablerApplication.sysCoap; }
        model.addAttribute("ledstate", "false (perhaps)");
        model.addAttribute("arg", appName);
        model.addAttribute("ledgui","ledOff");
        Colors.out("HumanEnablerController welcomePage" + model + " sysClient=" + appl);
        //Colors.out("HumanEnablerController sonar active=" + appl.sonarIsactive()  );
        //appl.sonarActivate();
        //allOnRasp = false;
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    @RequestMapping("/basic")
    public String basicPage(Model model) {
        if( appl == null ){ appl = MsenablerApplication.sysMqtt; }
        model.addAttribute("ledstate", "false (perhaps)");
        model.addAttribute("arg", appName);
        model.addAttribute("ledgui","ledOff");
        Colors.out("HumanEnablerController welcomePage" + model + " sysClient=" + appl);
        Colors.out("HumanEnablerController sonar active=" + appl.sonarIsactive()  );
        //allOnRasp = true;
        return "RadarSystemUserGui";  
    }


    @PostMapping(path = "/setApplAddress")
    public String setApplAddress(@RequestParam(name="cmd", required=false, defaultValue="")String addr , Model viewmodel )  {
    	if( ! addr.equals( "localhost" ) ){
            viewmodel.addAttribute("viewmodelarg", "configured with basicrobot addr="+addr);
            viewmodel.addAttribute("applicationlAddr",  addr);
        }else{
            viewmodel.addAttribute("viewmodelarg", "localhost not allowed");
        }
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    @PostMapping( path = "/on" )
    public String doOn( @RequestParam(name="cmd", required=false, defaultValue="")
                    String moveName, Model model){
    	Colors.out("HumanEnablerController doOn " + appl  );
        if( appl != null ){
            appl.ledActivate(true);
            String ledState = appl.ledState();
            Colors.out("HumanEnablerController doOn ledState=" + ledState  );
            model.addAttribute("ledgui","ledOn");
            model.addAttribute("ledstate", ledState);
            
            appl.sonarActivate();
        }
        model.addAttribute("arg", appName+" After Led on");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }



    @PostMapping( path = "/off" )
    public String doOff(@RequestParam(name="cmd", required=false, defaultValue="")
                        String moveName, Model model){
        //activateTheClient();
        //if( sysClient == null ){ sysClient = MsenablerApplication.sys; }
        if( appl != null ){
            appl.ledActivate(false);
            String ledState = appl.ledState();
            Colors.out("HumanEnablerController doOff ledState=" + ledState  );
            model.addAttribute("ledgui","ledOff");
            model.addAttribute("ledstate", ledState);
            
            appl.sonarDectivate();
        }
         model.addAttribute("arg", appName+" After Led off");
         if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/doLedBlink" )
    public String doBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                String moveName, Model model){
        //if( appl != null ) appl.doLedBlink( );
        model.addAttribute("arg", appName+" After Led blink");
        model.addAttribute("ledstate","ledBlinking ...");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/stopLedBlink" )
    public String stopLedBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        //if( appl != null ) appl.stopLedBlink( );
//        String ledState = appl.ledState();
//        Colors.out("HumanEnablerController stopLedBlink ledState=" + ledState  );
        model.addAttribute("arg", appName+" After Led stop blink");
//        if(ledState.equals("true"))  model.addAttribute("ledgui","ledOn");
//        else model.addAttribute("ledgui","ledOff");
//        model.addAttribute("ledstate",ledState);
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/distance" )
    public String distance(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        String d = "unknown";
        Colors.out("HumanEnablerController DISTANCE - sonar active=" + appl.sonarIsactive()  );
        if( appl != null ){
            if( ! appl.sonarIsactive() ) appl.sonarActivate();
            d = appl.sonarDistance();
            Colors.out("HumanEnablerController sonar d=" + d + " sonarDelay=" + RadarSystemConfig.sonarDelay );
        }            	
        model.addAttribute("arg", appName+" After distance");
        model.addAttribute("sonardistance",d);
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/sonardataon" )
    public String sonardataon(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
//    	if( sonarDataOn ) {
//    		if( basicGui ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
//    	}
        Colors.out("HumanEnablerController sonardataon - sonar active=" + appl.sonarIsactive()  );
        if( appl != null ){
            if( ! appl.sonarIsactive() ) appl.sonarActivate();
            //sonarDataOn = true;
            /*
            WebSocketHandler h = WebSocketHandler.getWebSocketHandler();
            new Thread() {
            	public void run() {
                    while( appl.sonarIsactive() && sonarDataOn ) {
                    	String d = appl.sonarDistance();
                    	Colors.out("HumanEnablerController sonar d=" + d + " sonarDelay=" + RadarSystemConfig.sonarDelay );
                        Utils.delay(RadarSystemConfig.sonarDelay);
                        //update on ws
                        try {
							h.sendToAll( d );
						} catch (Exception e) {
 							Colors.outerr("ws update ERROR:" + e.getMessage());
 							sonarDataOn = false;
 							appl.sonarDectivate();
						}
                    }   
                    sonarDataOn = false;
            	}
            }.start();*/
        }
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    
    @PostMapping( path = "/sonardataoff" )
    public String sonardataoff(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
    	//sonarDataOn = false;
    	appl.sonarDectivate();
        if( allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    
    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "HumanEnablerController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }

}
