package it.unibo.sonarguigpring

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

class WebPageHandler(val controller: HIController) : CoapHandler {
    var counter = 0
    override fun onLoad(response: CoapResponse) {
        val content: String = response.getResponseText()
        //response={"sonarvalue":"D"} or {"info":"somevalue"}
        try {
            val jsonContent = JSONObject(content)
            //if (jsonContent.has("sonarvalue")) return //sonar data are inserted in the model
            if (jsonContent.has("info")      ) {
                /* The resource shows something new (e.g. an alarm) */
                sysUtil.colorPrint(
                    "WebPageHandler | value: $content simpMessagingTemplate=${controller.simpMessagingTemplate}",
                    Color.BLUE
                )
                val info = ResourceRep("" + HtmlUtils.htmlEscape( jsonContent.getString("info")) + ":"+ counter++)
                sysUtil.colorPrint("WebPageHandler | info=${info.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, info)
            }
        }catch(e:Exception){
            sysUtil.colorPrint("WebPageHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("WebPageHandler  |  FAILED  ")
    }
}