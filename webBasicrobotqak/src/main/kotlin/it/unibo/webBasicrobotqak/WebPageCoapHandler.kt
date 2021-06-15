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
        if( content.contains("stepFail") ||
            content.contains("stepDone") ||
            content.contains("info")) {
                val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( content )  )
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
        }
        /*
        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("info")      ) {
                /* The resource shows something new  */
                sysUtil.colorPrint("WebPageCoapHandler | value: $content simpMessagingTemplate=${controller.simpMessagingTemplate}", Color.BLUE)
                //controller.simpMessagingTemplate?.convertAndSend("/topic/infodisplay", infoRep)
            }
        }catch(e:Exception){
            sysUtil.colorPrint("WebPageCoapHandler | ERROR=${content}", Color.RED)
        }*/
    }

    override fun onError() {
        System.err.println("WebPageCoapHandler  |  FAILED  ")
    }
}