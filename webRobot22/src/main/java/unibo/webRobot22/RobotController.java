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
import unibo.actor22comm.utils.ColorsOut;

//---------------------------------------------------
//import unibo.Robots.common.RobotUtils;


@Controller 
public class RobotController {
    @Value("${spring.server.protocol}")
    String protocol;

    protected static  String robotName     = ""; //visibility in package
    protected String mainPage   = "basicrobot22Gui";
    protected boolean usingCoap = false;
    public RobotController() {

    }



  @GetMapping("/") 		 
  public String entry(Model viewmodel) {
     if( usingCoap ) viewmodel.addAttribute("protocol", "coap");
     else viewmodel.addAttribute("protocol", protocol);
  	 return mainPage;
  }

    @PostMapping("/configure")
    public String configure(Model viewmodel, @RequestParam String ipaddr, String addr ){
        System.out.println("RobotHIController | configure:" + ipaddr );
         //Uso basicrobto22 sulla porta 8020
        robotName  = "basicrobot";
        if( usingCoap ) RobotUtils.connectWithRobotUsingCoap(ipaddr+":8020");
        else RobotUtils.connectWithRobotUsingTcp(ipaddr);
        return mainPage;
    }

    @PostMapping("/robotmove")
    public String doMove(Model viewmodel  , @RequestParam String move ){
        ColorsOut.outappl("RobotController | doMove:" + move + " robotName=" + robotName, ColorsOut.BLUE);
        //WebSocketConfiguration.wshandler.sendToAll("RobotController | doMove:" + move); //disappears
        try {
            //String msg = RobotUtils.moveAril(robotName,move).toString();
            //ColorsOut.outappl("RobotController | doMove msg:" + msg , ColorsOut.BLUE);
            //conn.forward( msg );
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

