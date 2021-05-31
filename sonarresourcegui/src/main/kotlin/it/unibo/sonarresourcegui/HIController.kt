package it.unibo.sonarresourcegui

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
//import kotlinx.coroutines.delay
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
https://spring.io/guides/gs/messaging-stomp-websocket/

 */


@Controller
class HIController {
    @Value("\${human.logo}")
    var appName: String?    = null

    //var coap    = CoapSupport("coap://localhost:8028", "ctxsonarresource/sonarresource")


    /*
     * Update the page vie socket.io when the application-resource changes.
     * See also https://www.baeldung.com/spring-websockets-send-message-to-user
     */
    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null
    init{
        //sysUtil.colorPrint("HumanInterfaceController | INIT", Color.GREEN)
        //coap.observeResource( WebPageHandler(this) ) //TODO: update the HTML page via socket
    }



    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName )
        //sysUtil.colorPrint("HIController | entry model=$model", Color.GREEN)
        return   "sonarGui"
    }

    @GetMapping("/sonardata")    //defines that the method handles GET requests.
    fun entryforpost(model: Model): String {
        model.addAttribute("arg", appName )
        sysUtil.colorPrint("HIController | entryforpost model=$model", Color.GREEN)
        return "sonarGui"
    }

    //curl -i -d "content=Post five" -X POST http://localhost:8083/posts
    //it is a good practice to return the location of the newly created resource in the response header.
    @PostMapping("/sonardata")
    fun  handleSonarValue(viewmodel : Model,
        @RequestParam(name="sonarvalue", required=false, defaultValue="0")v : String) : String{
           return "sonarGui"
     }




}