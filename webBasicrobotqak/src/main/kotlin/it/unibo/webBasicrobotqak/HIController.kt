package it.unibo.webBasicrobotqak

import com.andreapivetta.kolor.Color
import connQak.connQakBase
import connQak.connQakTcp
import it.unibo.actor0.sysUtil
import it.unibo.basicrobot.Basicrobot
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
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

 */

@Controller
class HIController {
    @Value("\${human.logo}")
    var appName: String?    = null
    /*
     * Update the page vie socket.io when the application-resource changes.
     * See also https://www.baeldung.com/spring-websockets-send-message-to-user
     */
    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null

    var basicrobot               : ActorBasic? = null
    lateinit var connSupport     : connQakBase
    lateinit var robotproxy      : ActorBasic
    lateinit var coapsupport     : CoapSupport
    val scopeTorunBasicrobot = CoroutineScope(Dispatchers.Default)
    //lateinit var basicrobotScope : CoroutineScope

    companion object{
        var logo = "just to test"
        //var answerMoveChannel  = Channel<String>()
        var robotanswerChannel  : Channel<String>? = null
        fun setAnswerChannel(answChannel  : Channel<String>){
            sysUtil.colorPrint("HIController | answChannelllllllllllll: ${answChannel}", Color.BLUE)
            robotanswerChannel = answChannel
        }

    }

    //var ws         = IssWsHttpJavaSupport.createForWs ("localhost:8083")
    init{
        try{
            //answerChannel = answerMoveChannel
            //sysUtil.colorPrint("HIController | answerChannel: ${answerChannel}", Color.BLUE)
            //connSupport = connQak.connQakBase.create( connQak.connprotocol )
            //connSupport.createConnection()

            scopeTorunBasicrobot.launch {
                 QakContext.createContexts(
                    "localhost", this, "basicrobot.pl", "sysRules.pl"
                )

                //answerChannel = (robotproxy as basicrobotProxy).answerMoveChannel
                //sysUtil.colorPrint("HIController | answerChannel: ${answerChannel}", Color.BLUE)
                //basicrobot = QakContext.getActor("basicrobot")!!
                //sysUtil.colorPrint("HIController | create basicrobot ${basicrobot}", Color.BLUE)
                //robotproxy = QakContext.getActor("basicrobotProxy")!!
                //sysUtil.colorPrint("HIController | create basicrobotProxy ${basicrobot}", Color.BLUE)
                //answerChannel = (robotproxy as basicrobotProxy).answerMoveChannel
                //basicrobotScope=basicrobot.scope
                //sysUtil.colorPrint("HIController | resource: ${resource.robotanswerChannel} scope=${basicrobotScope}", Color.BLUE)

             }
        }catch( e: Exception){
            sysUtil.colorPrint("HIController | Error $e", Color.RED)
        }
        while( basicrobot == null ){
            Thread.sleep(500)
            val br = QakContext.getActor("basicrobot")
            if( br == null ){
                sysUtil.colorPrint("HIController | waiting for basicrobot creation ...", Color.GREEN)
            }else basicrobot = br
        }
        coapsupport = CoapSupport("coap://localhost:8020", "ctxbasicrobot/basicrobot")
        coapsupport.observeResource( WebPageCoapHandler(this) )

        sysUtil.colorPrint("HIController | INIT", Color.GREEN)
        //connQakSupport = connQakCoap()
        //connQakSupport.createConnection()
        //coap.observeResource( WebPageCoapHandler(this) ) //TODO: update the HTML page via socket
    }



    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(viewmodel: Model): String {
         viewmodel.addAttribute("viewmodelarg", appName)
        sysUtil.colorPrint("HIController | entry model=$viewmodel", Color.GREEN)
        return  "basicrobotqakGui"
    }

    //http://localhost:8085/moveOnget?move=w  WARNING: not RESTful
    @GetMapping("/moveOnget")    //defines that the method handles GET requests.
    fun doMoveOnGet(viewmodel: Model,
        @RequestParam(name="move", required=false, defaultValue="h")robotmove : String
    ): String {
        viewmodel.addAttribute("viewmodelarg", robotmove)
        sysUtil.colorPrint("HIController | doMoveOnGet=$robotmove", Color.RED)
        return  "basicrobotqakGui"
    }


    //curl -i -d "move=w" -X POST http://localhost:8085/robotmove
    //it is a good practice to return the location of the newly created resource in the response header.
    @PostMapping("/robotmove")
    fun  doMove(viewmodel : Model,
        @RequestParam(name="move", required=false, defaultValue="h")robotmove : String) : String{
        sysUtil.colorPrint("HIController | param-move:$robotmove ", Color.RED)

        viewmodel.addAttribute("viewmodelarg", "${robotmove}") //resRep.content
         //connSupport.forward( MsgUtil.buildDispatch("webgui", "cmd", "cmd($robotmove)", connQak.qakdestination))
        //sysUtil.colorPrint("HIController | xxxxxxxxx... ${answerChannel} ", Color.RED)
        if( robotmove == "p" ){
            runBlocking{
                val reqMsg = MsgUtil.buildRequest("basicrobotproxy", "step", "step(350)", connQak.qakdestination)
                basicrobot!!.autoMsg(reqMsg)
                //handle answer
                //polling on the basicrobot ActorResourceRep since no shared channel is possible
                //between basicrobotProxy and HIController
                /*
                 var info = "unknown"
                 while( true ){
                     sysUtil.colorPrint("HIController | info=${info}", Color.RED)
                     if( info.contains("stepDone") ) break
                     if( info.contains("stepFail") ) break
                     delay( 350 ) //give time to change ActorResourceRep in step(T)
                     info = basicrobot!!.geResourceRep()
                 }
                 viewmodel.addAttribute("viewmodelarg", "${info}")*/
            }
            //sysUtil.colorPrint("HIController | returnnnnnnnn: ", Color.RED)
        }else {
            val cmdMsg = MsgUtil.buildDispatch("basicrobotproxy", "cmd", "cmd($robotmove)", connQak.qakdestination)
            scopeTorunBasicrobot.launch { basicrobot!!.autoMsg(cmdMsg) }    //basicrobot.actor.send(cmdMsg)
        }
        //When doing a POST, we do not return the HTML page
        return "basicrobotqakGui"
    }
/*
    @MessageMapping("/app/update")
    @SendTo("/topic/infodisplay")
    fun updateTheMap(@Payload message: String): ResourceRep {
        sysUtil.colorPrint("HIController | message=$message", Color.BLUE)
        return ResourceRep("todo")
    }
*/
}