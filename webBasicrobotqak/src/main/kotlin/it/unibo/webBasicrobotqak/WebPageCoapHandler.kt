package it.unibo.webBasicrobotqak

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

/*
An object of this class is registered as observer of the resource
 */
    class WebPageCoapHandler(val controller: HIController, var channel: Channel<String>? = null ) : CoapHandler {
    var counter = 0
    override fun onLoad(response: CoapResponse) {
        val content: String = response.getResponseText()
        //sysUtil.colorPrint("WebPageCoapHandler | content=$content", Color.LIGHT_MAGENTA )
        if( content.contains("stepDone") || content.contains("stepFail") ) {  //|| content.contains("info")
            val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( content )  )
            //Thread.sleep(400)   //otherwise updates before page return
            sysUtil.colorPrint("WebPageCoapHandler | content=$content", Color.LIGHT_MAGENTA )
            controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
            //controller.moveresult = content
            if( channel != null ) runBlocking{ channel!!.send("$content") }

        } /* else if( content.contains("stepFail") ){
            val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( content )  )
             sysUtil.colorPrint("WebPageCoapHandler | content=$content", Color.LIGHT_MAGENTA )
            controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
            //controller.moveresult = content
            runBlocking{ channel.send("resume") }
        } */
            else if( content.contains("obstacle") || content.contains("collision") ){
            val infoRep = ResourceRep("" + HtmlUtils.htmlEscape( "obstacle" )  )
            controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, infoRep)
        } //else controller.moveresult = content

    }

    override fun onError() {
        System.err.println("WebPageCoapHandler  |  FAILED  ")
    }
}