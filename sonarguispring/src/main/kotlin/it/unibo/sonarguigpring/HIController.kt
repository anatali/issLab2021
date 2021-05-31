package it.unibo.sonarguigpring

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.util.HtmlUtils


/*
The HIController USES the sonarresources via CoAP
See also it.unibo.boundaryWalk/userdocs/websocketInteraction.html
https://spring.io/guides/gs/messaging-stomp-websocket/

 */


@Controller
class HIController {
    @Value("\${human.logo}")
    var appName: String?    = null

    var coap    = CoapSupport("coap://localhost:8028", "ctxsonarresource/sonarresource")


    /*
     * Update the page vie socket.io when the application-resource changes.
     * See also https://www.baeldung.com/spring-websockets-send-message-to-user
     */
    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null

    //var ws         = IssWsHttpJavaSupport.createForWs ("localhost:8083")
    init{
        sysUtil.colorPrint("HumanInterfaceController | INIT", Color.GREEN)
        //connQakSupport = connQakCoap()
        //connQakSupport.createConnection()
        coap.observeResource( WebPageCoapHandler(this) ) //TODO: update the HTML page via socket
    }



    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName )
        sysUtil.colorPrint("HIController | entry model=$model", Color.GREEN)
        return  "sonarGui"
    }
/*
    @GetMapping("/sonardata")    //defines that the method handles GET requests.
    fun entryforpost(model: Model): String {
        model.addAttribute("arg", appName )
        sysUtil.colorPrint("HIController | entryforpost model=$model", Color.GREEN)
        return "sonarGui"
    }
*/
    //curl -i -d "content=Post 33" -X POST http://localhost:8083/sonardata
    //it is a good practice to return the location of the newly created resource in the response header.
    @PostMapping("/sonardata")
    fun  handleSonarValue(viewmodel : Model,
        @RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String{
        sysUtil.colorPrint("HIController | set sonar value:$v", Color.RED)

        coap.updateResourceWithValue(v)
        Thread.sleep(400);  //QUITE A LONG TIME ...
        val resourceRep = coap.readResource()
        val resRep      = ResourceRep( ""+ HtmlUtils.htmlEscape(resourceRep))
        sysUtil.colorPrint("HIController | resRep:$resRep", Color.BLUE)

         viewmodel.addAttribute("sonarval", "${resourceRep}") //resRep.content
         return "sonarGui"

        //val rep = getWebPageRep()
        //viewmodel.addAttribute("arg", "Current Robot State:  ${rep.content});
        //return "sonarGui"
    }

    fun getWebPageRep(): ResourceRep {
        val resourceRep: String = coap.readResource()
        println("HIController | resourceRep=$resourceRep")
        return ResourceRep("" + HtmlUtils.htmlEscape(resourceRep))
    }

    /*
     * Update the page via socket.io. Thanks to Eugenio Cerulo
     * https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/messaging/handler/annotation/MessageMapping.html
     * https://spring.io/guides/gs/messaging-stomp-websocket/
     * https://www.toptal.com/java/stomp-spring-boot-websocket
     */
    /*
    @MessageMapping("/app/update")
    @SendTo("/topic/infodisplay")
    fun updateTheMap(@Payload message: RequestMessageOnSock): ResourceRep {
        sysUtil.colorPrint("HIController | message=$message", Color.BLUE)
        return ResourceRep("todo")
    }
*/

}