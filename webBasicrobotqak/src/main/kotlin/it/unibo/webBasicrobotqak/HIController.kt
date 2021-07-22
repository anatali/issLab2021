package it.unibo.webBasicrobotqak

import com.andreapivetta.kolor.Color
import connQak.ConnectionType
import connQak.connQakBase
import it.unibo.actor0.sysUtil
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired

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

    //lateinit var connSupport     : connQakBase
    lateinit var coapsupport     : CoapSupport
    lateinit var connToRobot     : connQakBase
    lateinit var robotAddr       : String
    val channel      = Channel<String>()
    val coapObserver = WebPageCoapHandler(this, null)
    var moveresult="done"
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
    fun configure(addr : String){
        connQak.robothostAddr = addr
        robotAddr             = addr
        if( addr == "localhost" ) {/*
            try {
                scopeTorunBasicrobot.launch {
                    QakContext.createContexts(
                        "localhost", this, "basicrobot.pl", "sysRules.pl"
                    )
                }
            } catch (e: Exception) {
                sysUtil.colorPrint("HIController | Error $e", Color.RED)
            }
            while (basicrobot == null) {
                Thread.sleep(500)
                val br = QakContext.getActor("basicrobot")
                if (br == null) {
                    sysUtil.colorPrint("HIController | waiting for basicrobot creation ...", Color.GREEN)
                } else basicrobot = br
            }*/
        }else { //basicrobotAddr != "localhost"
            //basicrobot = null
            connToRobot = connQakBase.create(ConnectionType.TCP)
            connToRobot.createConnection()
        }
        coapsupport =
            CoapSupport("coap://${connQak.robothostAddr}:${connQak.robotPort}",
                "ctxbasicrobot/basicrobot")
        coapsupport.observeResource( coapObserver ) //WebPageCoapHandler(this, channel)

        sysUtil.colorPrint("HIController | INIT $simpMessagingTemplate", Color.GREEN)
    }//configure



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
        viewmodel.addAttribute("robotAddr",  robotAddr) //always ...
        sysUtil.colorPrint("HIController | doMoveOnGet=$robotmove", Color.RED)
        return  "basicrobotqakGui"
    }

    @PostMapping("/configure")
    fun handleConfigure(viewmodel : Model,
      @RequestParam(name="move", required=false, defaultValue="h")addr : String) : String {
        if( addr!="localhost"){
            configure(addr)
            viewmodel.addAttribute("viewmodelarg", "configured with basicrobot addr="+addr)
            viewmodel.addAttribute("robotAddr",  robotAddr)
        }else{
            viewmodel.addAttribute("viewmodelarg", "localhost not allowed")
        }

        return  "basicrobotqakGui"
    }


    //curl -i -d "move=w" -X POST http://localhost:8085/robotmove
    //it is a good practice to return the location of the newly created resource in the response header.
    @PostMapping("/robotmove")
    fun  doMove(viewmodel : Model,
        @RequestParam(name="move", required=false, defaultValue="h")robotmove : String) : String{
        sysUtil.colorPrint("HIController | param-move:$robotmove ", Color.RED)

         //viewmodel.addAttribute("viewmodelarg", "${robotmove}") //resRep.content
         //connSupport.forward( MsgUtil.buildDispatch("webgui", "cmd", "cmd($robotmove)", connQak.qakdestination))
        //sysUtil.colorPrint("HIController | xxxxxxxxx... ${answerChannel} ", Color.RED)
        if( robotmove == "p" ){
            val reqMsg = MsgUtil.buildRequest("basicrobotproxy", "step", "step(350)", connQak.qakdestination)
            /*
            Set the channel in coapObserver, so to receive the answer, to be shown on the page
             */
            coapObserver.channel = channel
            connToRobot.request(reqMsg)
            sysUtil.colorPrint("HIController | waits on channel ", Color.BLUE)
            runBlocking{
                moveresult = channel.receive()
                sysUtil.colorPrint("HIController | receives $moveresult", Color.BLUE)
                //Reset the channel in coapObserver, so to allow normal updating
                coapObserver.channel = null
            }
        }else { //normal move
            val cmdMsg = MsgUtil.buildDispatch("hicontroller", "cmd", "cmd($robotmove)", connQak.qakdestination)
            //if( basicrobot !=null )  scopeTorunBasicrobot.launch { basicrobot!!.autoMsg(cmdMsg) }    //basicrobot.actor.send(cmdMsg)
            //else
                connToRobot.forward( cmdMsg )
        }
        //
        viewmodel.addAttribute("viewmodelarg", "${robotmove}:${moveresult}")
        viewmodel.addAttribute("robotAddr",  robotAddr) //always ...
        moveresult = "done"
        //sysUtil.colorPrint("HIController | return the page after move ", Color.RED)
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