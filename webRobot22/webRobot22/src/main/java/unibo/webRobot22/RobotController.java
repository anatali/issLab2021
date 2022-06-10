package unibo.webRobot22;
//https://www.baeldung.com/websockets-spring
//https://www.toptal.com/java/stomp-spring-boot-websocket


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller 
public class RobotController { 

	//String htmlPage  = "index";
    String htmlPage  = "robotGuiPost";
    //String htmlPage  = "robotGuiSocket";
    //String htmlPage  = "robotGuiPostBoundary"; 

    public RobotController() {

    }



  @GetMapping("/") 		 
  public String entry(Model viewmodel) {
 	 viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ");
  	 return htmlPage;
  } 

    
 
/*
 * curl --location --request POST 'http://localhost:8080/move' --header 'Content-Type: text/plain' --form 'move=l'	
 * curl -d move=r localhost:8080/move
 */
}

