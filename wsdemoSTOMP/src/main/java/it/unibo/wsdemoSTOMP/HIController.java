package it.unibo.wsdemoSTOMP;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

@Controller
public class HIController {
	
	@RequestMapping("/")
	public String entryMinimal() {
		System.out.println("HIController | entryMinimal   "  );
		return "indexNoImages";
	}

	
	@MessageMapping("/unibo")	 
	@SendTo("/demoTopic/output")
	public OutputMessage elabInput(InputMessage message) throws Exception {
		System.out.println("HIController | elabInput  message=" + message.getName());
		//HtmlUtils.htmlEscape prepara il nome nel messaggio di input ad essere reso nel DOM lato client
		return new OutputMessage("Elaborated: " + HtmlUtils.htmlEscape(message.getName()) + " ");
	}
	
}
