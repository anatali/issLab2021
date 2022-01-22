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

import java.io.File;
import java.util.Base64;


@Controller
public class HumanEnablerController {
	private boolean allOnRasp   = MsenablerApplication.allOnRasp;
	private boolean sonarDataOn = false;
	private String raspAddr     = "unkown";
    private boolean webCamActive= false;
    private boolean applStarted = false;
    private String ledState     = "false";
    private String photoFName   = "curPhoto.jpg";
    public static IApplicationFacade appl; //RadarSystemDevicesOnRaspMqtt appl;

    @Value("${unibo.application.name}")
    String appName;

    @GetMapping("/")
    //@RequestMapping("/")
    public String welcomePage(Model model) {    	
        //if( appl == null ){  appl = allOnRasp ? MsenablerApplication.sysMqtt : MsenablerApplication.sysCoap; }
        model.addAttribute("ledstate", ledState+"(perhaps)");
        model.addAttribute("arg", appName);
        model.addAttribute("ledgui","ledOff");
        Colors.out("HumanEnablerController welcomePage" + model + " sysClient=" + appl);
        //Colors.out("HumanEnablerController sonar active=" + appl.sonarIsactive()  );
        //appl.sonarActivate();
        //allOnRasp = false;
        setModelValues(model,"entry");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }


    @PostMapping(path = "/setApplAddress")
    public String setApplAddress(@RequestParam(name="cmd", required=false, defaultValue="")
                                             String addr , Model viewmodel )  {
    	if( ! addr.equals( "localhost" ) ){
            //viewmodel.addAttribute("viewmodelarg", "configured with basicrobot addr="+addr);
            Colors.out("HumanEnablerController setApplAddress " + addr  );
            raspAddr = addr;

            //if( appl == null ){
                //if( allOnRasp ) appl = MsenablerApplication.startSystemMqtt();
                //else appl = MsenablerApplication.startSystemCoap(addr);
                //}
          }else{
            //viewmodel.addAttribute("viewmodelarg", "localhost not allowed");
        }
        setModelValues(viewmodel,"setApplAddress");
    	if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }


    @PostMapping(path = "/startAppl")
    public String startAppl(@RequestParam(name="cmd", required=false, defaultValue="")
                                       String value , Model viewmodel ) {
        if( allOnRasp ) appl = MsenablerApplication.startSystemMqtt();
        else appl = MsenablerApplication.startSystemCoap(raspAddr);
        applStarted = true;
        setModelValues(viewmodel, "startAppl");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";

    }


    @PostMapping( path = "/on" )
    public String doOn( @RequestParam(name="cmd", required=false, defaultValue="")
                    String moveName, Model model){
    	Colors.out("HumanEnablerController doOn " + appl  );
        if( appl != null ){
            appl.ledActivate(true);
            ledState = appl.ledState();
            Colors.out("HumanEnablerController doOn ledState=" + ledState  );
            model.addAttribute("ledgui","ledOn");
            model.addAttribute("ledstate", ledState);
            
            //appl.sonarActivate();  //useful in debug ...
        }
        setModelValues(model,"Led on");
         if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }



    @PostMapping( path = "/off" )
    public String doOff(@RequestParam(name="cmd", required=false, defaultValue="")
                        String moveName, Model model){
        //activateTheClient();
        //if( sysClient == null ){ sysClient = MsenablerApplication.sys; }
        if( appl != null ){
            appl.ledActivate(false);
            ledState = appl.ledState();
            Colors.out("HumanEnablerController doOff ledState=" + ledState  );
            model.addAttribute("ledgui","ledOff");
            model.addAttribute("ledstate", ledState);
            
            //appl.sonarDectivate(); //useful in debug ...
        }
        setModelValues(model,"Led off");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/doLedBlink" )
    public String doBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                String moveName, Model model){
        //if( appl != null ) appl.doLedBlink( );
        model.addAttribute("ledstate","ledBlinking ...");
        setModelValues(model,"Led blink");
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
        setModelValues(model,"Led blink stop");
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
        model.addAttribute("sonardistance",d);
        setModelValues(model,"Distance");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/sonardataon" )
    public String sonardataon(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
         Colors.out("HumanEnablerController sonardataon - sonar active=" + appl.sonarIsactive()  );
        if( appl != null ){
            if( ! appl.sonarIsactive() ) appl.sonarActivate();
        }
        setModelValues(model,"sonardataon");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    
    @PostMapping( path = "/sonardataoff" )
    public String sonardataoff(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
    	//sonarDataOn = false;
    	appl.sonarDectivate();
        setModelValues(model,"sonardataoff");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/takephoto" )
    public String getPhoto(@RequestParam(name="cmd", required=false, defaultValue="")
                                       String moveName, Model model) {
        Colors.out("HumanEnablerController takephoto-0 "    );
        appl.takePhoto(photoFName);
        //Colors.out("HumanEnablerController takephoto-1 "    );
        //Occorre fare richiesta e attendere risposta
        //String photoBase64 = appl.getImage(photoFName);
        //Colors.out("HumanEnablerController photoBase64 length=" + photoBase64.length()  );
        //appl.storeImage(photoBase64,photoFName);


        //File f = new File(photoFName);
        //leggo il contenuto del file (image) e lo invio alla zona output della pagina
        //addImageToWindow(image)
        model.addAttribute("photo", "PHOTO in "+photoFName);
        setModelValues(model,"takephoto");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    @PostMapping( path = "/showphoto" )
    public String showPhoto(@RequestParam(name="cmd", required=false, defaultValue="")
                                   String moveName, Model model) {
        //String photoBase64 = appl.getImage(photoFName);
        //Colors.out("HumanEnablerController showPhoto: " + photoBase64  );
        //appl.storeImage(photoBase64,"curPhoto.jpg");
        //model.addAttribute("photo", "PHOTO in "+photoFName);
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping(path = "/webCamActive")
    public String webCamActive(@RequestParam(name="cmd", required=false, defaultValue="")
                                       String value , Model viewmodel ) {
          if( ! webCamActive ) {
            appl.startWebCamStream();
            webCamActive = true;
        }
        setModelValues(viewmodel,"webCamActive");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    @PostMapping(path = "/webCamStop")
    public String webCamStop(@RequestParam(name="cmd", required=false, defaultValue="")
                                       String value , Model viewmodel ) {
          if(  webCamActive ) {
            appl.stopWebCamStream();
            webCamActive = false;
        }
        setModelValues(viewmodel,"webCamActive");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }


protected void setModelValues(Model model, String info){
    model.addAttribute("arg", appName+" - action=" + info);
    model.addAttribute("applicationAddr",  raspAddr);
    model.addAttribute("webCamActive",  webCamActive);
    model.addAttribute("applStarted",  applStarted);
    model.addAttribute("ledState",  ledState);
}

    
    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity( "HumanEnablerController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }

}

/*
    @PostMapping( path = "/startWebCamStream" )
    public String startWebCamStream(@RequestParam(name="cmd", required=false, defaultValue="")
                                   String moveName, Model model) {
        appl.startWebCamStream();
        setModelValues(model,"startWebCamStream");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
*/
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