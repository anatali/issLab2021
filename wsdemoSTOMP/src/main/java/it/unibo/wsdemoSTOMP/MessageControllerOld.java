package it.unibo.wsdemoSTOMP;
 
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

//@Controller  //Replaced by HIController
public class MessageControllerOld {
//Il Controller cerca i files nella directory template

	@RequestMapping("/")
	public String entryMinimal() {
		System.out.println("MessageController | entryMinimal   "  );
		return "indexNoImages";  //usa wsStompMinimal.js
	}

	@RequestMapping("/better")
	public String entryBetter() {
		return "indexBetter";	 //usa wsStompBetter.js
	}


	@MessageMapping("/inputmsg")
	@SendTo("/demoApp/output")
	public OutputMessage elabInput(InputMessage message) throws Exception {
		System.out.println("MessageController | elabInput  message="  );
		//System.out.println("MessageController | elabInput  message=" + message.getName());
		//Thread.sleep(500); // simulated delay
		//HtmlUtils.htmlEscape prepara il nome nel messaggio di input ad essere reso nel DOM lato client
		return new OutputMessage("Elaborated: " + HtmlUtils.htmlEscape(message.getName()) + " ");
	}

}
 