package it.unibo.sonarguigpring

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
        sysUtil.colorPrint("WebPageCoapHandler | response content=$content}", Color.GREEN )
        //response={"sonarvalue":"D"} or {"info":"somevalue"}
        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("sonarvalue")){
                val sonarRep = ResourceRep("" + HtmlUtils.htmlEscape( jsonContent.getString("sonarvalue"))  )
                sysUtil.colorPrint("WebPageCoapHandler | sonar value=${sonarRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, sonarRep)
                return
            }
            if (jsonContent.has("info")      ) {
                /* The resource shows something new  */
                //sysUtil.colorPrint("WebPageCoapHandler | value: $content simpMessagingTemplate=${controller.simpMessagingTemplate}", Color.BLUE)
                val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( jsonContent.getString("info")) + ":"+ counter++)
                sysUtil.colorPrint("WebPageCoapHandler | info=${infoRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
            }
        }catch(e:Exception){
            sysUtil.colorPrint("WebPageCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("WebPageCoapHandler  |  FAILED  ")
    }
}