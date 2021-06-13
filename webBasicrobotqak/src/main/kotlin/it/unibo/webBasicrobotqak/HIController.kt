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

    lateinit var connSupport : connQakBase
    lateinit var basicrobot  : ActorBasic
    val scopeTorunBasicrobot = CoroutineScope(Dispatchers.Default)

    /*
     * Update the page vie socket.io when the application-resource changes.
     * See also https://www.baeldung.com/spring-websockets-send-message-to-user
     */
    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null

    //var ws         = IssWsHttpJavaSupport.createForWs ("localhost:8083")
    init{
        try{
            //connSupport = connQak.connQakBase.create( connQak.connprotocol )
            //connSupport.createConnection()
            scopeTorunBasicrobot.launch {
                 QakContext.createContexts(
                    "localhost", this, "basicrobot.pl", "sysRules.pl"
                )
                basicrobot = QakContext.getActor("basicrobot")!!
                sysUtil.colorPrint("HIController | create basicrobot ${basicrobot}", Color.BLUE)
            }

        }catch( e: Exception){
            sysUtil.colorPrint("HIController | Error $e", Color.RED)
        }
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
        sysUtil.colorPrint("HIController | param-move:$robotmove", Color.RED)
        viewmodel.addAttribute("viewmodelarg", "${robotmove}") //resRep.content
        val cmdMsg = MsgUtil.buildDispatch("webgui", "cmd", "cmd($robotmove)", connQak.qakdestination)
        //connSupport.forward( MsgUtil.buildDispatch("webgui", "cmd", "cmd($robotmove)", connQak.qakdestination))
        scopeTorunBasicrobot.launch { basicrobot.autoMsg(cmdMsg) }    //basicrobot.actor.send(cmdMsg)
        return "basicrobotqakGui"

    }


}