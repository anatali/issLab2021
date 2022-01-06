package it.unibo.wsdemoSTOMP;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

	@RequestMapping("/")
	public String entry() {
		return "indexNoImages";
	}

	@MessageMapping("/inputmsg")    //un msg inviato a /inputmsg induce l'esecuzione del metodo
	@SendTo("/topic/output")	 //la risposta è inviata ai subscribers di /topic/output
	public OutputMessage elabInput(InputMessage message) throws Exception {
		System.out.println("MessageController | elabInput  message=" + message.getName());
		Thread.sleep(500); // simulated delay
		//HtmlUtils.htmlEscape prepara il nome nel messaggio di input ad essere reso nel DOM lato client
		return new OutputMessage("Elaborated: " + HtmlUtils.htmlEscape(message.getName()) + " ");
	}

}
