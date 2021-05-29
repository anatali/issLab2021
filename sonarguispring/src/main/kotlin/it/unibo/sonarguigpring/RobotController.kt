package it.unibo.sonarguigpring
//https://www.baeldung.com/websockets-spring
//https://www.toptal.com/java/stomp-spring-boot-websocket

import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil.buildDispatch
import it.unibo.kactor.MsgUtil.buildRequest
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.HtmlUtils
import java.lang.Exception
import java.util.*
//Imported from robotWeb2020 to check
//@Controller
class RobotController {
    var appName = "robotGui"
    var viewModelRep = "startup"
    var robotHost = "" //ConnConfig.hostAddr;		
    var robotPort = "" //ConnConfig.port;

    //String htmlPage  = "robotGuiPost"; 
    var htmlPage = "robotGuiSocket"

    //String htmlPage  = "robotGuiPostBoundary"; 
    var robotMoves: MutableSet<String> = HashSet()
    lateinit var connQakSupport: connQakCoap

    /*
     * Update the page vie socket.io when the application-resource changes.
     * Thanks to Eugenio Cerulo
     */
    @Autowired
    var simpMessagingTemplate: SimpMessagingTemplate? = null
    @GetMapping("/")
    fun entry(viewmodel: Model): String {
        viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ")
        peparePageUpdating()
        return htmlPage
    }

    @GetMapping("/applmodel")
    @ResponseBody
    fun getApplicationModel(viewmodel: Model?): String? {
        val rep = webPageRep
        return rep.content
    }

    @PostMapping(path = ["/move"])
    fun doMove(
        @RequestParam(name = "move", required = false, defaultValue = "h") moveName: String, viewmodel: Model
    ): String {
        println("------------------- RobotController doMove move=$moveName")
        if (robotMoves.contains(moveName)) {
            doBusinessJob(moveName, viewmodel)
        } else {
            viewmodel.addAttribute("arg", "Sorry: move unknown - Current Robot State:$viewModelRep")
        }
        return htmlPage
        //return "robotGuiSocket";  //ESPERIMENTO
    }

    private fun peparePageUpdating() {
        connQakSupport.client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("RobotController --> CoapClient changed ->" + response.getResponseText())
                simpMessagingTemplate?.convertAndSend(
                    WebSocketConfig.topicForClient,
                    ResourceRep("" + HtmlUtils.htmlEscape(response.getResponseText()))
                )
            }

            override fun onError() {
                println("RobotController --> CoapClient error!")
            }
        })
    }

    /*
	 * INTERACTION WITH THE BUSINESS LOGIC			
	 */
    protected fun doBusinessJob(moveName: String?, viewmodel: Model?) {
        try {
            if (moveName == "p") {
                val msg: ApplMessage =
                    buildRequest("web", "step", "step(" + configurator.stepsize + ")", configurator.qakdest)
                connQakSupport.request(msg)
            } else {
                val msg: ApplMessage = buildDispatch("web", "cmd", "cmd($moveName)", configurator.qakdest)
                connQakSupport.forward(msg)
            }
            //WAIT for command completion ...
            Thread.sleep(400) //QUITE A LONG TIME ...
            if (viewmodel != null) {
                val rep = webPageRep
                viewmodel.addAttribute("arg", "Current Robot State:  " + rep.content)
            }
        } catch (e: Exception) {
            println("------------------- RobotController doBusinessJob ERROR=" + e.message)
            //e.printStackTrace();
        }
    }

    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<String> {
        val responseHeaders = HttpHeaders()
        return ResponseEntity<String>(
            "RobotController ERROR " + ex.message, responseHeaders, HttpStatus.CREATED
        )
    }

    /* ----------------------------------------------------------
   Message-handling Controller
  ----------------------------------------------------------
 */
    //	@MessageMapping("/hello")
    //	@SendTo("/topic/display")
    //	public ResourceRep greeting(RequestMessageOnSock message) throws Exception {
    //		Thread.sleep(1000); // simulated delay
    //		return new ResourceRep("Hello by AN, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    //	}
    @MessageMapping("/move")
    @SendTo("/topic/display")
    @Throws(Exception::class)
    fun backtoclient(message: RequestMessageOnSock): ResourceRep {
// 		ApplMessage msg = MsgUtil.buildDispatch("web", "cmd", "cmd("+message.getName()+")", "basicrobot" );
//		connQakSupport.forward( msg );
//		System.out.println("------------------- RobotController forward=" + msg  );
        doBusinessJob(message.name, null)
        //		//WAIT for command completion ...
//		Thread.sleep(400);
        return webPageRep
    }

    @MessageMapping("/update")
    @SendTo("/topic/display")
    fun updateTheMap(@Payload message: String?): ResourceRep {
        return webPageRep
    }

    /*
 * The @MessageMapping annotation ensures that, 
 * if a message is sent to the /hello destination, the greeting() method is called.    
 * The payload of the message is bound to a HelloMessage object, which is passed into greeting().
 * 
 * Internally, the implementation of the method simulates a processing delay by causing 
 * the thread to sleep for one second. 
 * This is to demonstrate that, after the client sends a message, 
 * the server can take as long as it needs to asynchronously process the message. 
 * The client can continue with whatever work it needs to do without waiting for the response.
 * 
 * After the one-second delay, the greeting() method creates a Greeting object and returns it. 
 * The return value is broadcast to all subscribers of /topic/display, 
 * as specified in the @SendTo annotation. 
 * Note that the name from the input message is sanitized, since, in this case, 
 * it will be echoed back and re-rendered in the browser DOM on the client side.
 */
    val webPageRep: ResourceRep
        get() {
            val resourceRep = connQakSupport.readRep()
            println("------------------- RobotController resourceRep=$resourceRep")
            return ResourceRep("" + HtmlUtils.htmlEscape(resourceRep))
        }

    /*
 * curl --location --request POST 'http://localhost:8080/move' --header 'Content-Type: text/plain' --form 'move=l'	
 * curl -d move=r localhost:8080/move
 */
    init {
        /*
        connQak.configurator.configure()
        htmlPage = connQak.configurator.getPageTemplate()
        robotHost = connQak.configurator.getHostAddr()
        robotPort = connQak.configurator.getPort()

         */
        robotMoves.addAll(Arrays.asList(*arrayOf("w", "s", "h", "r", "l", "z", "x", "p")))
        connQakSupport = connQakCoap()
        connQakSupport.createConnection()
    }
}