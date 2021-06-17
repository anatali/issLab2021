package it.unibo.webBasicrobotqak

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

/*
An object of this class is registered as observer of the resource
 */
    class WebPageCoapHandler(val controller: HIController) : CoapHandler {
    var counter = 0
    override fun onLoad(response: CoapResponse) {
        val content: String = response.getResponseText()
        sysUtil.colorPrint("WebPageCoapHandler | content=$content", Color.LIGHT_MAGENTA )
        if( content.contains("stepDone") || content.contains("info")) {
            val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( content )  )
            controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
        } else if( content.contains("stepFail") ){
            val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( content )  )
            Thread.sleep(350)   //otherwise does not updates ... ???
            controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
        } else if( content.contains("obstacle") || content.contains("collision") ){
            val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( "obstacle" )  )
            controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
        }

    }

    override fun onError() {
        System.err.println("WebPageCoapHandler  |  FAILED  ")
    }
}