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
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapHandler



/*
The HIController USES the sonarresources via CoAP
See also it.unibo.boundaryWalk/userdocs/websocketInteraction.html
 */


@Controller
class HIController {
    @Value("\${human.logo}")
    var appName: String?    = null

    var coap    = CoapSupport("coap://192.168.1.45:8028", "ctxsonarresource/sonarresource")
    //lateinit var connQakSupport:connQakCoap

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
        coap.observeResource( WebPageHandler(this) ) //TODO: update the HTML page via socket
    }



    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName )
        sysUtil.colorPrint("HIController | entry model=$model", Color.GREEN)
        //peparePageUpdating()
        return "sonarGui"
    }
/*
    fun peparePageUpdating() {
        connQakSupport.client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                sysUtil.colorPrint("HIController --> CoapClient changed -> ${response.responseText}", Color.BLUE )
                simpMessagingTemplate?.convertAndSend(
                    WebSocketConfig.topicForClient,
                    ResourceRep("" + HtmlUtils.htmlEscape(response.responseText))
                )
            }

            override fun onError() {
                println("HIController --> CoapClient error!")
            }
        })
    }

 */
    @PostMapping( "/sonardata" )
    fun  handleSonarValue(model : Model,
        @RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String): String{
        sysUtil.colorPrint("HIController | set sonar value:$v", Color.RED)
        //connQakSupport.updateResourceWithValue(v)
        coap.updateResourceWithValue(v)
        model.addAttribute("sonarval", v)
        return "sonarGui"
    }
    /*
     * Update the page via socket.io. Thanks to Eugenio Cerulo
     * https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/messaging/handler/annotation/MessageMapping.html
     * https://spring.io/guides/gs/messaging-stomp-websocket/
     * https://www.toptal.com/java/stomp-spring-boot-websocket
     */
    @MessageMapping("/update")
    @SendTo("/topic/infodisplay")
    fun updateTheMap(@Payload message: RequestMessageOnSock): ResourceRep {
        sysUtil.colorPrint("HIController | message=$message", Color.BLUE)
        return ResourceRep("todo")
    }


}