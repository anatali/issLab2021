package unibo.sonarCmdGui

import it.unibo.kactor.MqttUtils
import it.unibo.kactor.MsgUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import unibo.actor22comm.interfaces.Interaction2021
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import unibo.sonarCmdGui.HIController
import unibo.actor22comm.utils.ColorsOut
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception

@Controller
class HIController {

    private val mainPage = "SonarCmdGui"

    init {
        ColorsOut.outappl("HIController: CREATE", ColorsOut.WHITE_BACKGROUND)

    }

    companion object {
        var sonarAddr = "" //visibility in package
        val mqtt      = MqttUtils("gui")
        val startMsg = MsgUtil.buildDispatch("gui", "sonaractivate", "info(ok)", "sonarqak22")
    }


    @Value("\${spring.application.name}")
    var appName: String? = null

    @GetMapping("/")
    fun homePage(model: Model): String {
        model.addAttribute("arg", appName)
        return mainPage
    }

    //Dopo click sul pulsante Configure
    @PostMapping("/configure")
    fun configure(viewmodel: Model?, @RequestParam addr: String): String {
        sonarAddr = addr
        ColorsOut.outappl("HIController | configure: $sonarAddr mqtt=$mqtt", ColorsOut.BLUE)

        mqtt.connect("gui", "tcp://broker.hivemq.com:1883")
        return mainPage
    }

    //Dopo click sul pulsante start/stop/resume
    @PostMapping("/sonarcmd")
    fun doCmd(viewmodel: Model?, @RequestParam cmd: String): String {
        //String @RequestParam(name="move", required=false, defaultValue="h")robotmove  )  {
        //sysUtil.colorPrint("HIController | param-move:$robotmove ", Color.RED)
        ColorsOut.outappl("HIController | sonarcmd $cmd  sonarAddr=$sonarAddr", ColorsOut.BLUE)
        if( cmd == "start") //println("todo: start")
            mqtt.publish( "unibo/nat/radar", startMsg.toString() )
        else println("todo: stop")
        return mainPage
    }

    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        return ResponseEntity<Any?>(
            "BaseController ERROR " + ex.message,
            responseHeaders, HttpStatus.CREATED
        )
    }


}