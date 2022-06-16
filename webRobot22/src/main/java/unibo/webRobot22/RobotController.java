package unibo.webRobot22;
//https://www.baeldung.com/websockets-spring
//https://www.toptal.com/java/stomp-spring-boot-websocket
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import unibo.Robots.common.RobotUtils;
import unibo.actor22comm.coap.CoapConnection;
import unibo.actor22comm.utils.ColorsOut;

//---------------------------------------------------
//import unibo.Robots.common.RobotUtils;


@Controller 
public class RobotController {
    @Value("${robot22.protocol}")
    String protocol;
    @Value("${robot22.webcamip}")
    String webcamip;
    @Value("${robot22.robotip}")
    String robotip;

    protected static  String robotName     = ""; //visibility in package
    protected String mainPage   = "basicrobot22Gui";
    protected boolean usingTcp  = false;

    public RobotController() {

    }

    protected void setConfigParams(Model viewmodel){
        viewmodel.addAttribute("protocol", protocol);
        viewmodel.addAttribute("webcamip", webcamip);
        viewmodel.addAttribute("robotip",  robotip);
    }

  @GetMapping("/") 		 
  public String entry(Model viewmodel) {
      setConfigParams(viewmodel);
      return mainPage;
  }

    @PostMapping("/setprotocol")
    public String setprotocol(Model viewmodel, @RequestParam String protocol  ){
        this.protocol = protocol;
        usingTcp      = protocol.equals("tcp");
        System.out.println("RobotController | setprotocol:" + protocol );
        viewmodel.addAttribute("protocol", protocol);
        setConfigParams(viewmodel);
        return mainPage;
    }
    @PostMapping("/setwebcamip")
    public String setwebcamip(Model viewmodel, @RequestParam String ipaddr  ){
        webcamip = ipaddr;
        System.out.println("RobotHIController | setwebcamip:" + ipaddr );
        viewmodel.addAttribute("webcamip", webcamip);
        setConfigParams(viewmodel);
        return mainPage;
    }
    @PostMapping("/setrobotip")
    public String setrobotip(Model viewmodel, @RequestParam String ipaddr  ){
        robotip = ipaddr;
        System.out.println("RobotHIController | setrobotip:" + ipaddr );
        viewmodel.addAttribute("robotip", robotip);
        setConfigParams(viewmodel);
        //Uso basicrobto22 sulla porta 8020
        robotName  = "basicrobot";
        if( usingTcp ) RobotUtils.connectWithRobotUsingTcp(ipaddr+":8020");
        //Attivo comunque una connessione CoAP per osservare basicrobot
        CoapConnection conn = RobotUtils.connectWithRobotUsingCoap(ipaddr+":8020");
        conn.observeResource( new RobotCoapObserver() );
        //WebSocketConfiguration.wshandler.sendToAll("Connected to Robot" + ipaddr); //disappears
        return mainPage;
    }

    @PostMapping("/robotmove")
    public String doMove(Model viewmodel  , @RequestParam String move ){
        ColorsOut.outappl("RobotController | doMove:" + move + " robotName=" + robotName, ColorsOut.BLUE);
        //WebSocketConfiguration.wshandler.sendToAll("RobotController | doMove:" + move); //disappears
        try {
              RobotUtils.sendMsg(robotName,move);
        } catch (Exception e) {
            ColorsOut.outerr("RobotController | doMove ERROR:"+e.getMessage());
        }
        return mainPage;
    }






    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "BaseController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }
 
/*
 * curl --location --request POST 'http://localhost:8080/move' --header 'Content-Type: text/plain' --form 'move=l'	
 * curl -d move=r localhost:8080/move
 */
}

