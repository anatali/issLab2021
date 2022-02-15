package it.unibo.msenabler;


import it.unibo.enablerCleanArch.domain.IApplicationFacade;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
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
//import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;

import java.io.File;
import java.util.Base64;


@Controller
public class HIController {
	private boolean allOnRasp   = MsenablerApplication.allOnRasp;
//	private boolean sonarDataOn = false;
//	private String raspAddr     = "unkown";
//    private boolean webCamActive= false;
//    private boolean applStarted = false;
//    private String ledState     = "false";
//    private String photoFName   = "curPhoto.jpg";
    
    private AdapterGui gui      = new AdapterGui();
    public static IApplicationFacade appl; //RadarSystemDevicesOnRaspMqtt appl;
    

    @Value("${unibo.application.name}")
    String appName;

    @GetMapping("/")
    //@RequestMapping("/")
    public String welcomePage(Model model) {    	
//        model.addAttribute("ledstate", ledState+"(perhaps)");
//        model.addAttribute("arg", appName);
//        model.addAttribute("ledgui","ledOff");
        gui.setApplName(model,appName);
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }


    @PostMapping(path = "/setApplAddress")
    public String setApplAddress(@RequestParam(name="cmd", required=false, defaultValue="")
                                             String addr , Model  model )  {
    	if( ! addr.equals( "localhost" ) ){ //NON siamo nel caso local
            appl = MsenablerApplication.startSystem(addr);
            if( Utils.isCoap() ) {
            	appl.activateObserver(WebSocketHandler.getWebSocketHandler());
            }else if( Utils.isMqtt() ) {
            	
            }
            gui.setApplStarted( model, addr);        
    	}
    	if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }


 
/*
LED
 */
    @PostMapping( path = "/on" )
    public String doOn( @RequestParam(name="cmd", required=false, defaultValue="")
                    String moveName, Model model){
    	ColorsOut.out("HumanEnablerController doOn " + appl  );
        appl.ledActivate(true);
        gui.setLedState(model, "true", "ledOn"); //appl.ledState()
        //setModelValues(model,"Led on");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }



    @PostMapping( path = "/off" )
    public String doOff(@RequestParam(name="cmd", required=false, defaultValue="")
                        String moveName, Model model){
         appl.ledActivate(false);
         gui.setLedState(model, appl.ledState(), "ledOff");            
         //setModelValues(model,"Led off");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/doLedBlink" )
    public String doBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                String moveName, Model model){
        appl.doLedBlink( );
        //model.addAttribute("ledstate","ledBlinking ...");
        gui.setLedState(model, "unknown", "ledBlink" );
        //setModelValues(model,"Led blink");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/stopLedBlink" )
    public String stopLedBlink(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        appl.stopLedBlink( );
        gui.setLedState(model, "unknown", "ledBlinkStop" );
//        model.addAttribute("ledstate","no led blink");
//        setModelValues(model,"Led blink stop");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/distance" )
    public String distance(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){
        String d = "unknown";
        ColorsOut.out("HumanEnablerController DISTANCE - sonar active=" + appl.sonarIsactive()  );
        if( ! appl.sonarIsactive() ) appl.sonarActivate();
        d = appl.sonarDistance();
        ColorsOut.out("HumanEnablerController sonar d=" + d + " sonarDelay=" + RadarSystemConfig.sonarDelay );
//         model.addAttribute("sonardistance",d);
//        setModelValues(model,"Distance");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }

    @PostMapping( path = "/sonardataon" )
    public String sonardataon(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
        ColorsOut.out("HumanEnablerController sonardataon - sonar active=" + appl.sonarIsactive()  );
        if( ! appl.sonarIsactive() ) appl.sonarActivate();
        gui.setSonarOn(model);
        //setModelValues(model,"sonardataon");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
    
    @PostMapping( path = "/sonardataoff" )
    public String sonardataoff(@RequestParam(name="cmd", required=false, defaultValue="")
                                  String moveName, Model model){      
     	appl.sonarDectivate();
    	 gui.setSonarOff(model);
        //setModelValues(model,"sonardataoff");
        if( ! allOnRasp ) return "RadarSystemUserGui"; else return "RadarSystemUserConsole";
    }
/*
    @PostMapping( path = "/takephoto" )
    public String getPhoto(@RequestParam(name="cmd", required=false, defaultValue="")
                                       String moveName, Model model) {
        ColorsOut.out("HumanEnablerController takephoto-0 "    );
        appl.takePhoto(photoFName);
        //Si dovrebbe gestire una risposta ...
        //Utils.delay(2000);
        //appl.sendCurrentPhoto();  //riceve una POST NON VA

         * Raspberry fa la foto e la invia al server con una POST
         * Il MachineEnablerController del server memorizza la foto sul file curPhoto.jpg
         * e poi invia la foto su tutte le connessioni correnti della ws.
         * Tutti ricevono la foto, tranne in browser che ha originato il processo.
         * a meno di non ritardare l'aggiornamento della ws

        //ColorsOut.out("HumanEnablerController takephoto-1 "    );
        //Occorre fare richiesta e attendere risposta
        //String photoBase64 = appl.getImage(photoFName);
        //ColorsOut.out("HumanEnablerController photoBase64 length=" + photoBase64.length()  );
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
        //ColorsOut.out("HumanEnablerController showPhoto: " + photoBase64  );
        //appl.storeImage(photoBase64,"curPhoto.jpg");
        //model.addAttribute("photo", "PHOTO in "+photoFName);
        appl.sendCurrentPhoto();
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

*/
    
    

    
    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity( "HIController ERROR " + ex.getMessage(),
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
                    	ColorsOut.out("HumanEnablerController sonar d=" + d + " sonarDelay=" + RadarSystemConfig.sonarDelay );
                        Utils.delay(RadarSystemConfig.sonarDelay);
                        //update on ws
                        try {
							h.sendToAll( d );
						} catch (Exception e) {
 							ColorsOut.outerr("ws update ERROR:" + e.getMessage());
 							sonarDataOn = false;
 							appl.sonarDectivate();
						}
                    }
                    sonarDataOn = false;
            	}
            }.start();*/